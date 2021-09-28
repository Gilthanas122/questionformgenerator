package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidInputFormatException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.questiontextpossibilities.QuestionTextPossibilityService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionFormService questionFormService;
  private final QuestionTextPossibilityService questionTextPossibilityService;
  private final AppUserService appUserService;
  private final QuestionConversionService questionConversionService;

  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionFormService questionFormService, QuestionTextPossibilityService questionTextPossibilityService, AppUserService appUserService, QuestionConversionService questionConversionService) {
    this.questionRepository = questionRepository;
    this.questionFormService = questionFormService;
    this.questionTextPossibilityService = questionTextPossibilityService;
    this.appUserService = appUserService;
    this.questionConversionService = questionConversionService;
  }

  @Override
  public void saveQuestion(String type, QuestionCreateDTO questionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException, InvalidInputFormatException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(questionDTO);
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    questionForm.setFinished(false);
    Question question;
    if (type.equals("text")) {
     question =  saveTextQuestion(questionDTO, questionForm);
    } else if (type.equals("checkbox")) {
      question =   saveCheckBoxQuestion(questionDTO, questionForm);
    } else if (type.equals("radio")) {
      question =   saveRadioQuestion(questionDTO, questionForm);
    } else if (type.equals("scale")) {
      question =   saveScaleQuestion(questionDTO, questionForm);
    }else{
      throw new InvalidInputFormatException("Invalid Question Type");
    }
    questionFormService.updateAnswerFormAfterAddingNewQuestion(questionForm, question);
  }

  @Override
  public QuestionWithDTypeDTO findByIdAndConvertToQuestionWithDTypeDTO(long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException {
    checkIfQuestionExistsById(questionId);
    Question question = questionRepository.findById(questionId);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(question.getQuestionForm().getAppUser().getId());
    return questionConversionService.convertFromQuestionToQuestionWithDType(question);
  }

  private void checkIfQuestionExistsById(long questionId) throws QuestionNotFoundByIdException {
    if (!questionRepository.existsById(questionId)) {
      throw new QuestionNotFoundByIdException("No question with the given id");
    }
  }

  @Override
  public Question findById(long questionId) throws QuestionNotFoundByIdException {
    checkIfQuestionExistsById(questionId);
    return questionRepository.findById(questionId);
  }

  //RE-TEST THIS SHIT
  @Override
  public Question saveQuestionFromQuestionDType(QuestionWithDTypeDTO questionWithDTypeDTO) throws QuestionNotFoundByIdException, MissingParamsException, BelongToAnotherUserException {
    if (!questionWithDTypeDTO.getQuestionType().equals(QuestionType.SCALEQUESTION.toString())){
      questionWithDTypeDTO.setScale(0);
    }
    questionWithDTypeDTO.setQuestionTextPossibilities(removeEmptyOrNullAnswerTextsFromQuestionTextPossibilities(questionWithDTypeDTO.getQuestionTextPossibilities()));
    ErrorServiceImpl.buildMissingFieldErrorMessage(questionWithDTypeDTO);
    Question question = findById(questionWithDTypeDTO.getId());
    Question converted = questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(question.getQuestionForm().getAppUser().getId());
    if (!question.getDiscriminatorValue().equals(converted.getDiscriminatorValue())) {
      throw new TypeMismatchException("The questionstypes do not match");
    }
    converted.setId(question.getId());
    converted.setListPosition(question.getListPosition());
    converted.setQuestionForm(question.getQuestionForm());
    questionRepository.save(converted);
    return converted;
  }

  private List<QuestionTextPossibility> removeEmptyOrNullAnswerTextsFromQuestionTextPossibilities(List<QuestionTextPossibility> questionTextPossibilities) {
    List<QuestionTextPossibility> questionTextPossibilitiesFiltered = questionTextPossibilities
            .stream().filter(Objects::nonNull)
            .filter(questionTextPossibility -> questionTextPossibility.getAnswerText() != null && !questionTextPossibility.getAnswerText().isEmpty())
            .collect(Collectors.toList());
    return questionTextPossibilitiesFiltered;
  }

  @Override
  public Question changeOrderOfQuestion(String change, long questionId) throws QuestionNotFoundByIdException, InvalidQuestionPositionException, InvalidQuestionPositionChangeException, BelongToAnotherUserException {
    Question question = findById(questionId);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(question.getQuestionForm().getAppUser().getId());
    if (!change.equals("up") && !change.equals("down")) {
      throw new InvalidQuestionPositionChangeException("Not valid parameter provided for changing the position");
    } else if (change.equals("up") && question.getListPosition() == 0) {
      throw new InvalidQuestionPositionException("Not possible to move element more forward. It's the first element");
    } else if (change.equals("down") && question.getQuestionForm().getQuestions().size() - 1 <= question.getListPosition()) {
      throw new InvalidQuestionPositionException("Not possible to move element more backward, it's the last element");
    }
    int currentPosition = question.getListPosition();
    Question questionToBeSwitchedWith = questionFormService.findQuestionToSwitchPositionWith(question.getQuestionForm(), currentPosition, change);
    question.setListPosition(questionToBeSwitchedWith.getListPosition());
    questionToBeSwitchedWith.setListPosition(currentPosition);
    questionRepository.saveAll(List.of(question, questionToBeSwitchedWith));
    return question;
  }

  @Override
  public long deleteQuestion(long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException {
    Question question = findById(questionId);

    appUserService.checkIfCurrentUserMatchesUserIdInPath(question.getQuestionForm().getAppUser().getId());
    questionRepository.setToBeDeleted(questionId);
    questionFormService.updateQuestionListPositionAfterDeletingQuestion(question.getQuestionForm());
    return question.getQuestionForm().getId();
  }

  @Override
  public long findQuestionFormIdBelongingToQuestion(long questionId) throws QuestionNotFoundByIdException {
    Question question = findById(questionId);
    return question.getQuestionForm().getId();
  }

  private Question saveScaleQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, InvalidInputFormatException {
    if (questionDTO.getAnswers().size() < 1) {
      throw new MissingParamsException("Should have a scale value for scale question");
    }

    if (!questionDTO.getAnswers().get(0).matches("[0-9]+")) {
      throw new InvalidInputFormatException("Scale value can only be a number");
    }
    ScaleQuestion scaleButtonQuestion = new ScaleQuestion(questionDTO.getQuestionText(), Integer.parseInt(questionDTO.getAnswers().get(0)));
    scaleButtonQuestion.setQuestionForm(questionForm);
    scaleButtonQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(scaleButtonQuestion);
    return scaleButtonQuestion;
  }

  private Question saveRadioQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    if (questionDTO.getAnswers().size() < 2) {
      throw new MissingParamsException("The provided number of answer possibilities is less than 2");
    }
    RadioButtonQuestion radioButtonQuestion = new RadioButtonQuestion(questionDTO.getQuestionText(), questionTextPossibilityService.convertStringToQuestionTextPossibility(questionDTO.getAnswers()));
    radioButtonQuestion.setQuestionForm(questionForm);
    radioButtonQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(radioButtonQuestion);
    return radioButtonQuestion;
  }

  private Question saveCheckBoxQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    if (questionDTO.getAnswers().size() < 2) {
      throw new MissingParamsException("The provided number of answer possibilities is less than 2");
    }
    CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionDTO.getQuestionText(), questionTextPossibilityService.convertStringToQuestionTextPossibility(questionDTO.getAnswers()));
    checkBoxQuestion.setQuestionForm(questionForm);
    checkBoxQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(checkBoxQuestion);

    return checkBoxQuestion;
  }

  private Question saveTextQuestion(QuestionCreateDTO textQuestionDTO, QuestionForm questionForm) throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException {
    TextQuestion textQuestion = new TextQuestion(textQuestionDTO.getQuestionText());
    textQuestion.setQuestionForm(questionForm);
    textQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(textQuestion);
    return textQuestion;
  }

  private int getListPositionForQuestions(QuestionForm questionForm) {
    if (questionForm.getQuestions() == null || questionForm.getQuestions().size() < 1) {
      return 0;
    }
    return questionForm.getQuestions().get(questionForm.getQuestions().size() - 1).getListPosition() + 1;
  }
}

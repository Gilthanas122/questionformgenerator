package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.*;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.deleteservice.DeleteService;
import com.bottomupquestionphd.demo.services.questions.questiontextpossibilities.QuestionTextPossibilityService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionFormService questionFormService;
  private final QuestionTextPossibilityService questionTextPossibilityService;
  private final AppUserService appUserService;
  private final QuestionConversionService questionConversionService;
  private final DeleteService deleteService;

  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionFormService questionFormService, QuestionTextPossibilityService questionTextPossibilityService, AppUserService appUserService, QuestionConversionService questionConversionService, DeleteService deleteService) {
    this.questionRepository = questionRepository;
    this.questionFormService = questionFormService;
    this.questionTextPossibilityService = questionTextPossibilityService;
    this.appUserService = appUserService;
    this.questionConversionService = questionConversionService;
    this.deleteService = deleteService;
  }

  @Override
  public void saveQuestion(String type, QuestionCreateDTO questionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException, InvalidInputFormatException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(questionDTO);
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    questionForm.setFinished(false);
    Question question;
    if (type.equals(QuestionType.TEXTQUESTION.toString())) {
      question = saveTextQuestion(questionDTO, questionForm);
    } else if (type.equals(QuestionType.CHECKBOXQUESTION.toString())) {
      question = saveCheckBoxQuestion(questionDTO, questionForm);
    } else if (type.equals(QuestionType.RADIOBUTTONQUESTION.toString())) {
      question = saveRadioQuestion(questionDTO, questionForm);
    } else if (type.equals(QuestionType.SCALEQUESTION.toString())) {
      question = saveScaleQuestion(questionDTO, questionForm);
    } else {
      throw new InvalidInputFormatException("Invalid Question Type");
    }
    questionFormService.updateAnswerFormAfterAddingNewQuestion(questionForm, question);
  }

  @Override
  public QuestionWithDTypeDTO findByIdAndConvertToQuestionWithDTypeDTO(long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException, QuestionHasBeenAnsweredException {
    Question question = findById(questionId);
    checkIfQuestionHasBeenAnswered(question);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(question.getQuestionForm().getAppUser().getId());
    return questionConversionService.convertFromQuestionToQuestionWithDType(question);
  }

  private void checkIfQuestionHasBeenAnswered(Question question) throws QuestionHasBeenAnsweredException {
    if (question.getAnswers().size() > 0) {
      throw new QuestionHasBeenAnsweredException("You can not modify a question where answers has been provided, only delete it");
    }
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

  @Override
  public Question saveQuestionFromQuestionDType(QuestionWithDTypeDTO questionWithDTypeDTO) throws QuestionNotFoundByIdException, MissingParamsException, BelongToAnotherUserException {
    if (!questionWithDTypeDTO.getQuestionType().equals(QuestionType.SCALEQUESTION.toString())) {
      questionWithDTypeDTO.setScale(0);
    } else if (questionWithDTypeDTO.getQuestionType().equals(QuestionType.RADIOBUTTONQUESTION.toString())
            || questionWithDTypeDTO.getQuestionType().equals(QuestionType.CHECKBOXQUESTION.toString())) {
      questionWithDTypeDTO.setQuestionTextPossibilities(setToDeletedQuestionTextPossibilitiesAndFilterOutEmptyField(questionWithDTypeDTO.getQuestionTextPossibilities()));
    }
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
    converted.setAnswers(question.getAnswers());
    questionRepository.save(converted);
    return converted;
  }

  private List<QuestionTextPossibility> setToDeletedQuestionTextPossibilitiesAndFilterOutEmptyField(List<QuestionTextPossibility> questionTextPossibilitiesFiltered) {
    for (int i = 0; i < questionTextPossibilitiesFiltered.size(); i++) {
      QuestionTextPossibility currentQuestionTextpossibility = questionTextPossibilitiesFiltered.get(i);
      if (currentQuestionTextpossibility.getAnswerText() == null || currentQuestionTextpossibility.getAnswerText().isEmpty()) {
        if (currentQuestionTextpossibility.getId() == 0) {
          questionTextPossibilitiesFiltered.remove(currentQuestionTextpossibility);
          i--;
        } else {
          questionTextPossibilitiesFiltered.set(i, deleteService.setQuestionTextPossibilityToBeDeleted(currentQuestionTextpossibility));
        }
      }
    }
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
    question = deleteService.setQuestionToBeDeleted(question);
    questionRepository.save(question);
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

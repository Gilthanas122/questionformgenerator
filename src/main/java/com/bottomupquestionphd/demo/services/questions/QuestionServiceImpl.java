package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.answerpossibilities.AnswerPossibilityService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionFormService questionFormService;
  private final AnswerPossibilityService answerPossibilityService;
  private final AppUserService appUserService;
  private final QuestionConversionService questionConversionService;


  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionFormService questionFormService, AnswerPossibilityService answerPossibilityService, AppUserService appUserService, QuestionConversionService questionConversionService) {
    this.questionRepository = questionRepository;
    this.questionFormService = questionFormService;
    this.answerPossibilityService = answerPossibilityService;
    this.appUserService = appUserService;
    this.questionConversionService = questionConversionService;
  }

  @Override
  public void saveQuestion(String type, QuestionCreateDTO questionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    questionForm.setFinished(false);
    if (type.equals("text")) {
      saveTextQuestion(questionDTO, questionForm);
    } else if (type.equals("checkbox")) {
      saveCheckBoxQuestion(questionDTO, questionForm);
    } else if (type.equals("radio")) {
      saveRadioQuestion(questionDTO, questionForm);
    } else if (type.equals("scale")) {
      saveScaleQuestion(questionDTO, questionForm);
    }
  }

  @Override
  public QuestionWithDTypeDTO findByIdAndConvertToQuestionWithDTypeDTO(long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException {
    Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundByIdException("No question with the given id"));
    if (question.getQuestionForm().getAppUser().getId() != appUserService.findCurrentlyLoggedInUser().getId()) {
      throw new BelongToAnotherUserException("Question with a given id belongs to a form of another user");
    }
    return questionConversionService.convertFromQuestionToQuestionWithDType(question);
  }

  @Override
  public Question findById(long questionIq) throws QuestionNotFoundByIdException {
    return questionRepository.findById(questionIq).orElseThrow(() -> new QuestionNotFoundByIdException("Couldn't find question with the given id"));
  }

  @Override
  public void saveQuestionFromQuestionDType(QuestionWithDTypeDTO questionWithDTypeDTO) throws QuestionNotFoundByIdException {
    Question question = findById(questionWithDTypeDTO.getId());
    Question converted = questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO);
    converted.setId(question.getId());
    converted.setQuestionForm(question.getQuestionForm());
    questionRepository.save(converted);
  }

  @Override
  public void changeOrderOfQuestion(String change, long questionId) throws QuestionNotFoundByIdException, InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    Question question = findById(questionId);
    if (!change.equals("up") && !change.equals("down")) {
      throw new InvalidQuestionPositionChangeException("Not valid parameter provided for changing the position");
    } else if (change.equals("up") && question.getListPosition() == 0) {
      throw new InvalidQuestionPositionException("Not possible to move element more forward. Is the first element");
    } else if (change.equals("down") && question.getQuestionForm().getQuestions().size() - 1 <= question.getListPosition()) {
      throw new InvalidQuestionPositionException("Not possible to move element more backward, is the last element");
    }
    int currentPosition = question.getListPosition();
    Question questionToBeSwitchedWith = questionFormService.findQuestionToSwitchPositionWith(question.getQuestionForm(), currentPosition, change);
    question.setListPosition(questionToBeSwitchedWith.getListPosition());
    questionToBeSwitchedWith.setListPosition(currentPosition);
    questionRepository.saveAll(List.of(question, questionToBeSwitchedWith));
  }

  @Override
  public long findQuestionFormIdBelongingToQuestion(long questionId) throws QuestionNotFoundByIdException {
    Question question = findById(questionId);
    return question.getQuestionForm().getId();
  }


  private void saveScaleQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException {
    if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().isEmpty()) {
      throw new MissingParamsException("Following input field(s) is missing: question text and/or scale");
    }
    ScaleQuestion scaleButtonQuestion = new ScaleQuestion(questionDTO.getQuestionText(), Integer.valueOf(questionDTO.getAnswers().get(0)));
    scaleButtonQuestion.setQuestionForm(questionForm);
    scaleButtonQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(scaleButtonQuestion);

  }

  private void saveRadioQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().isEmpty() || questionDTO.getAnswers().size() < 2) {
      throw new MissingParamsException("Following input field is missing: question text or provided number of answers is less than 2");
    }
    RadioButtonQuestion radioButtonQuestion = new RadioButtonQuestion(questionDTO.getQuestionText(), answerPossibilityService.converStringsToAnswerPossibilities(questionDTO.getAnswers()));
    radioButtonQuestion.setQuestionForm(questionForm);
    radioButtonQuestion.setListPosition(getListPositionForQuestions(questionForm));;
    questionRepository.save(radioButtonQuestion);
  }

  private void saveCheckBoxQuestion(QuestionCreateDTO questionDTO, QuestionForm questionForm) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().isEmpty() || questionDTO.getAnswers().size() < 2) {
      throw new MissingParamsException("Following input field is missing: question text or provided number of answers is less than 2");
    }
    CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionDTO.getQuestionText(), answerPossibilityService.converStringsToAnswerPossibilities(questionDTO.getAnswers()));
    checkBoxQuestion.setQuestionForm(questionForm);
    checkBoxQuestion.setListPosition(getListPositionForQuestions(questionForm));;
    questionRepository.save(checkBoxQuestion);

  }

  private void saveTextQuestion(QuestionCreateDTO textQuestionDTO, QuestionForm questionForm) throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException {
    if (textQuestionDTO.getQuestionText() == null || textQuestionDTO.getQuestionText().isEmpty()) {
      throw new MissingParamsException("Following input field is missing: question text");
    }
    TextQuestion textQuestion = new TextQuestion(textQuestionDTO.getQuestionText());
    textQuestion.setQuestionForm(questionForm);
    textQuestion.setListPosition(getListPositionForQuestions(questionForm));
    questionRepository.save(textQuestion);
  }

  private int getListPositionForQuestions(QuestionForm questionForm) {
    if (questionForm.getQuestions().size() < 1) {
      return 0;
    }
    return questionForm.getQuestions().get(questionForm.getQuestions().size() - 1).getListPosition() + 1;
  }
}

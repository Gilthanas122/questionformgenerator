package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.CheckBoxQuestion;
import com.bottomupquestionphd.demo.domains.daos.questions.TextQuestion;
import com.bottomupquestionphd.demo.domains.dtos.question.CheckBoxQuestionDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.TextQuestionDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.answerpossibilities.AnswerPossibilityService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionFormService questionFormService;
  private final AnswerPossibilityService answerPossibilityService;

  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionFormService questionFormService, AnswerPossibilityService answerPossibilityService) {
    this.questionRepository = questionRepository;
    this.questionFormService = questionFormService;
    this.answerPossibilityService = answerPossibilityService;
  }

  @Override
  public void saveQuestion(String type, QuestionCreateDTO questionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException {
    if (type.equals("text")){
      saveTextQuestion( questionDTO, questionFormId);
    }if (type.equals("checkbox")){
      saveCheckBoxQuestion(questionDTO, questionFormId);
    }
  }

  private void saveCheckBoxQuestion(QuestionCreateDTO questionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException {
    if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().isEmpty() || questionDTO.getAnswers().size() < 2){
      throw new MissingParamsException("Following input field is missing: question text or provided number of answers is less than 2");
    }
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionDTO.getQuestionText(), answerPossibilityService.converStringsToAnswerPossibilities(questionDTO.getAnswers()));
    checkBoxQuestion.setQuestionForm(questionForm);
    questionRepository.save(checkBoxQuestion);

  }

  private void saveTextQuestion(QuestionCreateDTO textQuestionDTO, long questionFormId) throws QuestionFormNotFoundException, MissingParamsException {
    if (textQuestionDTO.getQuestionText() == null || textQuestionDTO.getQuestionText().isEmpty()){
      throw new MissingParamsException("Following input field is missing: question text");
    }
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    TextQuestion textQuestion = new TextQuestion(textQuestionDTO.getQuestionText());
    textQuestion.setQuestionForm(questionForm);
    questionRepository.save(textQuestion);
  }
}

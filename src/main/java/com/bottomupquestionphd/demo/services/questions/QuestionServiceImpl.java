package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.TextQuestion;
import com.bottomupquestionphd.demo.domains.dtos.question.TextQuestionDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionFormService questionFormService;

  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionFormService questionFormService) {
    this.questionRepository = questionRepository;
    this.questionFormService = questionFormService;
  }

  @Override
  public void saveQuestion(String type, Object textQuestionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException {
    if (type.equals("text")){
      saveTextQuestion((TextQuestionDTO) textQuestionDTO, questionFormId);
    }
  }

  private void saveTextQuestion(TextQuestionDTO textQuestionDTO, long questionFormId) throws QuestionFormNotFoundException, MissingParamsException {
    if (textQuestionDTO.getQuestionText() == null || textQuestionDTO.getQuestionText().isEmpty()){
      throw new MissingParamsException("Following input field is missing: question text");
    }
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    TextQuestion textQuestion = new TextQuestion(textQuestionDTO.getQuestionText());
    textQuestion.setQuestionForm(questionForm);
    questionRepository.save(textQuestion);
  }
}

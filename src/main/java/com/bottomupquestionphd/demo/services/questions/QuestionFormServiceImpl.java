package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository) {
    this.questionFormRepository = questionFormRepository;
  }

  @Override
  public long save(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException {
    QuestionForm questionForm = validateQuestionCreateDTO(questionFormCreateDTO);
    questionFormRepository.save(questionForm);
    return questionForm.getId();
  }

  private QuestionForm validateQuestionCreateDTO(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException {
    if (questionFormCreateDTO.getDescription() == null || questionFormCreateDTO.getDescription().isEmpty()
  || questionFormCreateDTO.getName() == null || questionFormCreateDTO.getName().isEmpty()){
      throw new MissingParamsException("Following parameter(s) are missing: name, description");
    }else if (questionFormRepository.existsByName(questionFormCreateDTO.getName())){
      throw new QuestionFormNameAlreadyExistsException("A questionform with this name" + questionFormCreateDTO.getName() + "already exists.");
    }
    return new QuestionForm(questionFormCreateDTO.getName(), questionFormCreateDTO.getDescription());
  }
}

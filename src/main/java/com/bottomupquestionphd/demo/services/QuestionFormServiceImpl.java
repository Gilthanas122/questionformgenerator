package com.bottomupquestionphd.demo.services;

import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;

public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository) {
    this.questionFormRepository = questionFormRepository;
  }
}

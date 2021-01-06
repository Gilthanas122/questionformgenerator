package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository) {
    this.questionFormRepository = questionFormRepository;
  }
}

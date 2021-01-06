package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Controller;

@Controller
public class QuestionFormController {
  private final QuestionFormService questionFormService;

  public QuestionFormController(QuestionFormService questionFormService) {
    this.questionFormService = questionFormService;
  }
}

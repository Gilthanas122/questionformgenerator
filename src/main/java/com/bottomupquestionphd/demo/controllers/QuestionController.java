package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.services.questions.QuestionService;
import org.springframework.stereotype.Controller;

@Controller
public class QuestionController {
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }
}

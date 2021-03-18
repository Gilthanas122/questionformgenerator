package com.bottomupquestionphd.demo.domains.dtos.question;

import java.util.ArrayList;
import java.util.List;

public class QuestionCreateDTO {
  private String questionText;
  private List<String> answers = new ArrayList<>();

  public QuestionCreateDTO(){}

  public QuestionCreateDTO(String questionText) {
    this.questionText = questionText;
  }

  public QuestionCreateDTO(String questionText, List<String> answers) {
    this.questionText = questionText;
    this.answers = answers;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public List<String> getAnswers() {
    return answers;
  }

  public void setAnswers(List<String> answers) {
    this.answers = answers;
  }
}

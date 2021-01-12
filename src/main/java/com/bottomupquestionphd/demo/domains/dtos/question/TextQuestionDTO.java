package com.bottomupquestionphd.demo.domains.dtos.question;

public class TextQuestionDTO {
  private String questionText;

  public TextQuestionDTO(){}

  public TextQuestionDTO(String questionText) {
    this.questionText = questionText;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }
}

package com.bottomupquestionphd.demo.domains.dtos.question;

public class QuestionCreateDTO {
  private String questionText;
  private String hoverText;
  private String type;

  public QuestionCreateDTO(){}

  public QuestionCreateDTO(String questionText, String hoverText, String type) {
    this.questionText = questionText;
    this.hoverText = hoverText;
    this.type = type;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getHoverText() {
    return hoverText;
  }

  public void setHoverText(String hoverText) {
    this.hoverText = hoverText;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

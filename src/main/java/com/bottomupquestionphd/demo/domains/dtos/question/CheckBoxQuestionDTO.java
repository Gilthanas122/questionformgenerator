package com.bottomupquestionphd.demo.domains.dtos.question;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxQuestionDTO {
  private String questionText;
  private List<String> answers = new ArrayList<>();

  public  CheckBoxQuestionDTO(){}

  public CheckBoxQuestionDTO(String questionText, List<String> answers) {
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

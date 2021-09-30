package com.bottomupquestionphd.demo.domains.dtos.answerform;

import java.util.ArrayList;
import java.util.List;

public class DisplayOneUserAnswersDTO {
  private List<String> answers = new ArrayList<>();
  private List<String> questionTexts = new ArrayList<>();

  public DisplayOneUserAnswersDTO() {
  }

  public DisplayOneUserAnswersDTO( List<String> answers, List<String> questionTexts) {
    this.answers = answers;
    this.questionTexts = questionTexts;
  }

  public List<String> getAnswers() {
    return answers;
  }

  public void setAnswers(List<String> answers) {
    this.answers = answers;
  }

  public List<String> getQuestionTexts() {
    return questionTexts;
  }

  public void setQuestionTexts(List<String> questionTexts) {
    this.questionTexts = questionTexts;
  }
}

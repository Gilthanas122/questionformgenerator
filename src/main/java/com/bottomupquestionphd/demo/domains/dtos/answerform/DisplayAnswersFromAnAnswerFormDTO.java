package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;

import java.util.ArrayList;
import java.util.List;

public class DisplayAnswersFromAnAnswerFormDTO {
  private List<String> questionTypes = new ArrayList<>();
  private List<Answer> answers = new ArrayList<>();
  private List<String> questionTexts = new ArrayList<>();

  public DisplayAnswersFromAnAnswerFormDTO() {
  }

  public DisplayAnswersFromAnAnswerFormDTO(List<String> questionTypes, List<Answer> answers, List<String> questionTexts) {
    this.questionTypes = questionTypes;
    this.answers = answers;
    this.questionTexts = questionTexts;
  }

  public List<String> getQuestionTypes() {
    return questionTypes;
  }

  public void setQuestionTypes(List<String> questionTypes) {
    this.questionTypes = questionTypes;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public List<String> getQuestionTexts() {
    return questionTexts;
  }

  public void setQuestionTexts(List<String> questionTexts) {
    this.questionTexts = questionTexts;
  }
}

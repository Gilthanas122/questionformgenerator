package com.bottomupquestionphd.demo.domains.dtos.answerformfilter;

import java.util.ArrayList;
import java.util.List;

public class ActualAnswerTextSearchTermResultDTO {
  private long answerId;
  private String questionType;
  private List<String> actualAnswers = new ArrayList<>();
  private double textAnswerVotesAverage;

  public ActualAnswerTextSearchTermResultDTO() {
  }

  public ActualAnswerTextSearchTermResultDTO(long answerId, String questionType, List<String> actualAnswers, double textAnswerVotesAverage) {
    this.answerId = answerId;
    this.questionType = questionType;
    this.actualAnswers = actualAnswers;
    this.textAnswerVotesAverage = textAnswerVotesAverage;
  }

  public long getAnswerId() {
    return answerId;
  }

  public void setAnswerId(long answerId) {
    this.answerId = answerId;
  }

  public List<String> getActualAnswers() {
    return actualAnswers;
  }

  public void setActualAnswers(List<String> actualAnswers) {
    this.actualAnswers = actualAnswers;
  }

  public double getTextAnswerVotesAverage() {
    return textAnswerVotesAverage;
  }

  public void setTextAnswerVotesAverage(double textAnswerVotesAverage) {
    this.textAnswerVotesAverage = textAnswerVotesAverage;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }
}

package com.bottomupquestionphd.demo.domains.dtos.question;

import com.bottomupquestionphd.demo.domains.daos.questions.AnswerPossibility;

import java.util.ArrayList;
import java.util.List;

public class QuestionWithDTypeDTO {
  private long id;
  private long questionFormId;
  private String questionType;
  private String questionText;
  private List<AnswerPossibility> answerPossibilities = new ArrayList<>();
  private Integer scale;

  public QuestionWithDTypeDTO() {
  }

  public QuestionWithDTypeDTO(long id, String questionText) {
    this.id = id;
    this.questionText = questionText;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public List<AnswerPossibility> getAnswerPossibilities() {
    return answerPossibilities;
  }

  public void setAnswerPossibilities(List<AnswerPossibility> answerPossibilities) {
    this.answerPossibilities = answerPossibilities;
  }

  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  public long getQuestionFormId() {
    return questionFormId;
  }

  public void setQuestionFormId(long questionFormId) {
    this.questionFormId = questionFormId;
  }
}

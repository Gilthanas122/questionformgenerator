package com.bottomupquestionphd.demo.domains.dtos.question;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;

import java.util.ArrayList;
import java.util.List;

public class QuestionWithDTypeDTO {
  private long id;
  private long questionFormId;
  private String questionType;
  private String questionText;
  private List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();
  private Integer scale;

  public QuestionWithDTypeDTO() {
  }

  public QuestionWithDTypeDTO(long id, String questionText) {
    this.id = id;
    this.questionText = questionText;
  }

  public QuestionWithDTypeDTO(long id, long questionFormId, String questionType, String questionText, Integer scale) {
    this.id = id;
    this.questionFormId = questionFormId;
    this.questionType = questionType;
    this.questionText = questionText;
    this.scale = scale;
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

  public List<QuestionTextPossibility> getQuestionTextPossibilities() {
    return questionTextPossibilities;
  }

  public void setQuestionTextPossibilities(List<QuestionTextPossibility> questionTextPossibilities) {
    this.questionTextPossibilities = questionTextPossibilities;
  }
}

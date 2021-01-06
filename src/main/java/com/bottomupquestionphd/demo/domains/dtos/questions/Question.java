package com.bottomupquestionphd.demo.domains.dtos.questions;

import javax.persistence.*;

@Entity
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String questionText;
  private String hoverText;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Question question;

  public Question(){}

  public Question(String questionText, String hoverText) {
    this.questionText = questionText;
    this.hoverText = hoverText;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }
}

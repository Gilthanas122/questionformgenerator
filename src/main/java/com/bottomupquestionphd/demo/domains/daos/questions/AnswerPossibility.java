package com.bottomupquestionphd.demo.domains.daos.questions;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "answerpossibilities")
@Where(clause="deleted=0")
public class AnswerPossibility {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String answerText;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private MultipleAnswerQuestion multipleAnswerQuestion;

  public AnswerPossibility(){}

  public AnswerPossibility(String answerText) {
    this.answerText = answerText;
  }

  public AnswerPossibility(long id, String answerText) {
    this.id = id;
    this.answerText = answerText;
  }

  public AnswerPossibility(long id, String answerText, MultipleAnswerQuestion multipleAnswerQuestion) {
    this.id = id;
    this.answerText = answerText;
    this.multipleAnswerQuestion = multipleAnswerQuestion;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public MultipleAnswerQuestion getMultipleAnswerQuestion() {
    return multipleAnswerQuestion;
  }

  public void setMultipleAnswerQuestion(MultipleAnswerQuestion multipleAnswerQuestion) {
    this.multipleAnswerQuestion = multipleAnswerQuestion;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}

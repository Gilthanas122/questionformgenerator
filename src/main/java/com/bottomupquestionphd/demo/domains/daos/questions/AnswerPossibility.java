package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.*;

@Entity
@Table(name = "answerpossibilities")
public class AnswerPossibility {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String answerText;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private MultipleAnswerQuestion multipleAnswerQuestion;

  public AnswerPossibility(){}

  public AnswerPossibility(String answerText) {
    this.answerText = answerText;
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
}

package com.bottomupquestionphd.demo.domains.daos.answers;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "actualanswertexts")
@Where(clause="deleted=0")
public class ActualAnswerText {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String answerText;
  private boolean deleted;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private Answer answer;

  public ActualAnswerText() {
  }

  public ActualAnswerText(Answer answer) {
    this.answer = answer;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Answer getAnswer() {
    return answer;
  }

  public void setAnswer(Answer answer) {
    this.answer = answer;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean actualAnswerTextIsNullOrEmpty(){
    return this.getAnswerText() == null || this.getAnswerText().isEmpty();
  }
}

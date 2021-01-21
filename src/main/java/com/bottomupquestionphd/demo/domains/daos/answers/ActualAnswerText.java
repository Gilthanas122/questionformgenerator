package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;

@Entity
@Table(name = "actualanswertexts")
public class ActualAnswerText {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

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
}

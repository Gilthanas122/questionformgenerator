package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;

@Entity
@Table(name = "multipleanswertexts")
public class CheckBoxAnswerText {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String answerText;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private CheckBoxAnswer checkBoxAnswer;

  public CheckBoxAnswerText(){}

  public CheckBoxAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public CheckBoxAnswer getCheckBoxAnswer() {
    return checkBoxAnswer;
  }

  public void setCheckBoxAnswer(CheckBoxAnswer checkBoxAnswer) {
    this.checkBoxAnswer = checkBoxAnswer;
  }
}

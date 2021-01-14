package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TextAnswer")
@DiscriminatorValue("TextAnswer")
public class TextAnswer extends Answer{
  private String answerText;

  public TextAnswer(){}

  public TextAnswer(String answerText) {
    this.answerText = answerText;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }
}

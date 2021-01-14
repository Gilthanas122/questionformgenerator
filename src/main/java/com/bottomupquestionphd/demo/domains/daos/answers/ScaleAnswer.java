package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;

@Entity(name = "ScaleAnswer")
@DiscriminatorValue("ScaleAnswer")
public class ScaleAnswer extends Answer {
  private int scaleAnswer;

  public ScaleAnswer(){}

  public ScaleAnswer(int scaleAnswer) {
    this.scaleAnswer = scaleAnswer;
  }

  public int getScaleAnswer() {
    return scaleAnswer;
  }

  public void setScaleAnswer(int scaleAnswer) {
    this.scaleAnswer = scaleAnswer;
  }
}

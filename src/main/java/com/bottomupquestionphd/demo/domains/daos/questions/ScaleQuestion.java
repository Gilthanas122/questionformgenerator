package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "ScaleQuestion")
@DiscriminatorValue("ScaleQuestion")
public class ScaleQuestion extends Question {
  private int scale;

  public ScaleQuestion() {

  }

  public ScaleQuestion(String questionText, int scale) {
    super(questionText);
    this.scale = scale;
  }

  public ScaleQuestion(long id, String questionText, Integer scale) {
    super(id, questionText);
    this.scale = scale;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }
}

package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "MultipleAnswerQuestion")
@DiscriminatorValue("MultipleAnswerQuestion")
public  abstract class MultipleAnswerQuestion extends Question{

  public MultipleAnswerQuestion(String questionText) {
    super(questionText);
  }

  public MultipleAnswerQuestion() {

  }
}

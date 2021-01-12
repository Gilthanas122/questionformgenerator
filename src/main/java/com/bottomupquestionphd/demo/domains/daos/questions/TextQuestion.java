package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TextQuestion")
@DiscriminatorValue("TextQuestion")
public class TextQuestion extends Question{

  public TextQuestion() {

  }

  public TextQuestion(String questionText) {
    super(questionText);
  }
}

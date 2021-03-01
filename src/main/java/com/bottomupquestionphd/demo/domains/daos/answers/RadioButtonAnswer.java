package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RadioButtonAnswer")
@DiscriminatorValue("RadioButtonAnswer")
public class RadioButtonAnswer extends Answer{

  public RadioButtonAnswer(){}

  public RadioButtonAnswer(ActualAnswerText actualAnswerTexts) {
    super(actualAnswerTexts);
  }
}

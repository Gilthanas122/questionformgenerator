package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RadioButtonAnswer")
@DiscriminatorValue("RadioButtonAnswer")
public class RadioButtonAnswer extends Answer {
  private String radioButtonAnswerText;

  public RadioButtonAnswer(){}

  public RadioButtonAnswer(String radioButtonAnswerText) {
    this.radioButtonAnswerText = radioButtonAnswerText;
  }

  public String getRadioButtonAnswerText() {
    return radioButtonAnswerText;
  }

  public void setRadioButtonAnswerText(String radioButtonAnswerText) {
    this.radioButtonAnswerText = radioButtonAnswerText;
  }
}

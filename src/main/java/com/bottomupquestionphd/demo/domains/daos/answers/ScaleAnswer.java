package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;

@Entity(name = "ScaleAnswer")
@DiscriminatorValue("ScaleAnswer")
public class ScaleAnswer extends Answer {

  public ScaleAnswer(){};

  public ScaleAnswer(ActualAnswerText actualAnswerText){
    super(actualAnswerText);
  }
}

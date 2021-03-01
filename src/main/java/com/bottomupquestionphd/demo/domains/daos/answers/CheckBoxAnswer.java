package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "CheckBoxAnswer")
@DiscriminatorValue("CheckBoxAnswer")
public abstract class CheckBoxAnswer extends Answer {

  public CheckBoxAnswer(){}

  public CheckBoxAnswer(List<ActualAnswerText> answerTexts) {
    super(answerTexts);
  }
}

package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "TextAnswer")
@DiscriminatorValue("TextAnswer")
public class TextAnswer extends Answer{

  public TextAnswer (){}

  public TextAnswer(List<ActualAnswerText> actualAnswerTexts) {
    super(actualAnswerTexts);
  }
}

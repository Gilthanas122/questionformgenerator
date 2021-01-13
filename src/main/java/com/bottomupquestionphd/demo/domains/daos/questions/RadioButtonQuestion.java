package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "RadioButtonQuestion")
@DiscriminatorValue("RadioButtonQuestion")
public class RadioButtonQuestion extends MultipleAnswerQuestion {

  public RadioButtonQuestion() {

  }
  public RadioButtonQuestion(String questionText, List<AnswerPossibility> answers) {
    super(questionText, answers);
  }

  public RadioButtonQuestion(long id, String questionText, List<AnswerPossibility> answerPossibilities) {
    super(id, questionText, answerPossibilities);
  }
}

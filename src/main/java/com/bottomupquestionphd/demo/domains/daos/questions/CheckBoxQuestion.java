package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "CheckBoxQuestion")
@DiscriminatorValue("CheckBoxQuestion")
public class CheckBoxQuestion extends MultipleAnswerQuestion{

  public CheckBoxQuestion() {

  }
  public CheckBoxQuestion(String questionText, List<AnswerPossibility> answerPossibilities) {
  super(questionText, answerPossibilities);
  }

  public CheckBoxQuestion(long id, String questionText, List<AnswerPossibility> answerPossibilities) {
    super(id, questionText, answerPossibilities);
  }
}

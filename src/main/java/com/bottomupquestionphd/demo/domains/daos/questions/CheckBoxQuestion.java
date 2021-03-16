package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "CheckBoxQuestion")
@DiscriminatorValue("CheckBoxQuestion")
public class CheckBoxQuestion extends MultipleAnswerQuestion{

  public CheckBoxQuestion() {

  }
  public CheckBoxQuestion(String questionText, List<QuestionTextPossibility> questionTextPossibilities) {
  super(questionText, questionTextPossibilities);
  }

  public CheckBoxQuestion(long id, String questionText, List<QuestionTextPossibility> questionTextPossibilities) {
    super(id, questionText, questionTextPossibilities);
  }
}

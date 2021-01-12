package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CheckBoxQuestion")
@DiscriminatorValue("CheckBoxQuestion")
public class CheckBoxQuestion extends MultipleAnswerQuestion{
  @OneToMany(mappedBy = "multipleAnswerQuestion", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  public List<AnswerPossibility> answerPossibilities = new ArrayList<>();

  public CheckBoxQuestion(String questionText, List<AnswerPossibility> answerPossibilities) {
    super(questionText);
    this.answerPossibilities = answerPossibilities;
  }

  public List<AnswerPossibility> getAnswerPossibilities() {
    return answerPossibilities;
  }

  public void setAnswerPossibilities(List<AnswerPossibility> answerPossibilities) {
    this.answerPossibilities = answerPossibilities;
  }
}

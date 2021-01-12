package com.bottomupquestionphd.demo.domains.daos.questions;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MultipleAnswerQuestion")
@DiscriminatorValue("MultipleAnswerQuestion")
public  abstract class MultipleAnswerQuestion extends Question{

  @OneToMany(mappedBy = "multipleAnswerQuestion", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  public List<AnswerPossibility> answerPossibilities = new ArrayList<>();


  public MultipleAnswerQuestion() {

  }

  public MultipleAnswerQuestion(String questionText, List<AnswerPossibility> answerPossibilities) {
    super(questionText);
    addAnswerToPost(answerPossibilities);
  }

  protected void addAnswerToPost(List<AnswerPossibility> answerPossibilities){
    for (AnswerPossibility answerPossibility: answerPossibilities) {
      this.answerPossibilities.add(answerPossibility);
      answerPossibility.setMultipleAnswerQuestion(this);
    }
  };

  public List<AnswerPossibility> getAnswerPossibilities() {
    return answerPossibilities;
  }

  public void setAnswerPossibilities(List<AnswerPossibility> answerPossibilities) {
    this.answerPossibilities = answerPossibilities;
  }

}

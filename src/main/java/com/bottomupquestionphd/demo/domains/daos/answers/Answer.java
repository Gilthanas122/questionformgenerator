package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="answers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Answer_Type")
public abstract class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToMany(mappedBy = "answer")
  private List<ActualAnswerText> actualAnswerTexts = new ArrayList<>();

  public Answer() {
  }

  public Answer(List<ActualAnswerText> actualAnswerTexts){
    this.actualAnswerTexts = actualAnswerTexts;
  }

  public Answer(ActualAnswerText actualAnswerText){
    this.actualAnswerTexts.add(actualAnswerText);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<ActualAnswerText> getActualAnswerTexts() {
    return actualAnswerTexts;
  }

  public void setActualAnswerTexts(List<ActualAnswerText> actualAnswerTexts) {
    this.actualAnswerTexts = actualAnswerTexts;
  }
}

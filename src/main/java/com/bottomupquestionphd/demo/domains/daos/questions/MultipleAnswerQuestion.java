package com.bottomupquestionphd.demo.domains.daos.questions;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MultipleAnswerQuestion")
@DiscriminatorValue("MultipleAnswerQuestion")
public abstract class MultipleAnswerQuestion extends Question {

  @OneToMany(mappedBy = "multipleAnswerQuestion", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();

  public MultipleAnswerQuestion() {
  }

  public MultipleAnswerQuestion(String questionText, List<QuestionTextPossibility> questionTextPossibilities) {
    super(questionText);
    addAnswerToPost(questionTextPossibilities);
  }

  public MultipleAnswerQuestion(String questionText, long id) {
    super(id, questionText);
  }

  public MultipleAnswerQuestion(long id, String questionText, List<QuestionTextPossibility> questionTextPossibilities) {
    super(id, questionText);
    this.questionTextPossibilities = questionTextPossibilities;
  }


  protected void addAnswerToPost(List<QuestionTextPossibility> questionTextPossibilities) {
    for (QuestionTextPossibility questionTextPossibility : questionTextPossibilities) {
      this.questionTextPossibilities.add(questionTextPossibility);
      questionTextPossibility.setMultipleAnswerQuestion(this);
    }
  }

  public List<QuestionTextPossibility> getQuestionTextPossibilities() {
    return questionTextPossibilities;
  }

  public void setQuestionTextPossibilities(List<QuestionTextPossibility> questionTextPossibilities) {
    this.questionTextPossibilities = questionTextPossibilities;
  }

  public List<String> getQuestionTextPossibilitiesTexts() {
    List<String> questionTextPossibilities = new ArrayList<>();
    this.questionTextPossibilities
            .stream()
            .forEach(questionTextPossibility -> questionTextPossibilities.add(questionTextPossibility.getAnswerText()));
    return questionTextPossibilities;
  }
}

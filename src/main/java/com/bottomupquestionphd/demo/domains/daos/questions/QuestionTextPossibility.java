package com.bottomupquestionphd.demo.domains.daos.questions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "questiontextpossibilities")
@Where(clause="deleted=0")
public class QuestionTextPossibility {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotBlank(message = "Name may not be blank")
  private String answerText;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference(value = "questionTextPossibilitysMultipleAnswerQuestion")
  private MultipleAnswerQuestion multipleAnswerQuestion;

  public QuestionTextPossibility(){}

  public QuestionTextPossibility(String answerText) {
    this.answerText = answerText;
  }

  public QuestionTextPossibility(long id, String answerText) {
    this.id = id;
    this.answerText = answerText;
  }

  public QuestionTextPossibility(long id, String answerText, MultipleAnswerQuestion multipleAnswerQuestion) {
    this.id = id;
    this.answerText = answerText;
    this.multipleAnswerQuestion = multipleAnswerQuestion;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    if (!answerText.isBlank()){
      this.answerText = answerText;
    }
  }

  public MultipleAnswerQuestion getMultipleAnswerQuestion() {
    return multipleAnswerQuestion;
  }

  public void setMultipleAnswerQuestion(MultipleAnswerQuestion multipleAnswerQuestion) {
    this.multipleAnswerQuestion = multipleAnswerQuestion;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}

package com.bottomupquestionphd.demo.domains.daos.questions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "questiontextpossibilities")
@Where(clause="deleted=0")
@Builder(toBuilder = true)
public class QuestionTextPossibility {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
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

  public QuestionTextPossibility(long id, String answerText, boolean deleted, MultipleAnswerQuestion multipleAnswerQuestion) {
    this.id = id;
    this.answerText = answerText;
    this.deleted = deleted;
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

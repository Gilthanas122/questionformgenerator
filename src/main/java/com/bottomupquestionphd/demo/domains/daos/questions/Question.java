package com.bottomupquestionphd.demo.domains.daos.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;

import javax.persistence.*;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Question_Type")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String questionText;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private QuestionForm questionForm;

  public Question(){}

  public Question(String questionText) {
    this.questionText = questionText;
  }

  public Question(long id, String questionText) {
    this.id = id;
    this.questionText = questionText;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public QuestionForm getQuestionForm() {
    return questionForm;
  }

  public void setQuestionForm(QuestionForm questionForm) {
    this.questionForm = questionForm;
  }
}

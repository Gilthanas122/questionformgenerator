package com.bottomupquestionphd.demo.domains.daos.questions;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Question_Type")
@Where(clause = "deleted=0")
public class Question implements Comparable<Question> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotNull
  private String questionText;
  private int listPosition;
  private boolean deleted;

  @Transient
  private List<String> answersNotFilledOutByUser = new ArrayList<>();

  @ManyToOne(cascade = {CascadeType.MERGE})
  @JsonBackReference(value = "questionsQuestionForm")
  private QuestionForm questionForm;

  @OneToMany(mappedBy = "question", cascade = {CascadeType.MERGE})
  @JsonManagedReference("answersQuestion")
  private List<Answer> answers = new ArrayList<>();

  public Question() {
  }

  public Question(String questionText) {
    this.questionText = questionText;
  }

  public Question(long id, String questionText) {
    this.id = id;
    this.questionText = questionText;
  }

  public Question(long id, String questionText, Integer listPosition) {
    this.id = id;
    this.questionText = questionText;
    this.listPosition = listPosition;
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

  public Integer getListPosition() {
    return listPosition;
  }

  public void setListPosition(int listPosition) {
    this.listPosition = listPosition;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public int compareTo(Question o) {
    return this.getListPosition().compareTo(o.getListPosition());
  }

  @Transient
  public String getDiscriminatorValue() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  public void addOneAnswer(Answer answer) {
    this.answers.add(answer);
  }

  public List<String> getAnswersNotFilledOutByUser() {
    return answersNotFilledOutByUser;
  }

  public void setAnswersNotFilledOutByUser(List<String> answersNotFilledOutByUser) {
    this.answersNotFilledOutByUser = answersNotFilledOutByUser;
  }
}

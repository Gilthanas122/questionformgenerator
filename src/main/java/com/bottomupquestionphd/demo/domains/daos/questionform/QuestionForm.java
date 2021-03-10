package com.bottomupquestionphd.demo.domains.daos.questionform;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "questionforms")
public class QuestionForm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private String description;
  private boolean finished;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private AppUser appUser;

  @OneToMany(mappedBy = "questionForm", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "questionForm",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private List<AnswerForm> answerForms = new ArrayList<>();

  public QuestionForm(){}

  public QuestionForm(List<AnswerForm> answerForms){
    this.answerForms = answerForms;
  }

  public QuestionForm(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AppUser getAppUser() {
    return appUser;
  }

  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public List<Question> getQuestions() {
    Collections.sort(questions);
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<AnswerForm> getAnswerForms() {
    return answerForms;
  }

  public void setAnswerForms(List<AnswerForm> answerForms) {
    this.answerForms = answerForms;
  }

  public void addAnswerForm(AnswerForm answerForm) {
    this.answerForms.add(answerForm);
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}

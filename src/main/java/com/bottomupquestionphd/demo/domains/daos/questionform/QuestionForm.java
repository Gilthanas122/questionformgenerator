package com.bottomupquestionphd.demo.domains.daos.questionform;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "questionforms")
@Where(clause = "deleted=0")
public class QuestionForm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotNull
  private String name;
  @NotNull
  private String description;
  private boolean finished;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference(value = "appUsersquestionForms")
  private AppUser appUser;

  @OneToMany(mappedBy = "questionForm", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference(value = "questionsQuestionForm")
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "questionForm", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference(value = "questionForm")
  private List<AnswerForm> answerForms = new ArrayList<>();

  public QuestionForm() {
  }

  public QuestionForm(List<AnswerForm> answerForms) {
    this.answerForms = answerForms;
  }

  public QuestionForm(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public QuestionForm(String name, String description, AppUser appUser) {
    this.name = name;
    this.description = description;
    this.appUser = appUser;
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

  public List<String> getQuestionTexts() {
    return this.getQuestions().stream().map(Question::getQuestionText).collect(Collectors.toList());
  }

  public List<String> getQuestionTypes() {
    return this.getQuestions().stream().map(Question::getDiscriminatorValue).collect(Collectors.toList());
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

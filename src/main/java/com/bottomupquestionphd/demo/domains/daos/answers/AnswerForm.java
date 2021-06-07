package com.bottomupquestionphd.demo.domains.daos.answers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answerforms")
@Where(clause = "deleted=0")
public class AnswerForm implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.MERGE})
  @JsonBackReference(value = "questionForm")
  private QuestionForm questionForm;

  @OneToMany(mappedBy = "answerForm", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference(value = "answersAnswerform")
  private List<Answer> answers = new ArrayList<>();

  @ManyToOne(cascade = {CascadeType.MERGE})
  @JsonBackReference
  private AppUser appUser;

  public AnswerForm() {
  }

  public AnswerForm(long id, QuestionForm questionForm, AppUser appUser) {
    this.id = id;
    this.questionForm = questionForm;
    this.appUser = appUser;
  }

  public AnswerForm(QuestionForm questionForm, List<Answer> answers) {
    this.questionForm = questionForm;
    this.answers = answers;
  }

  public AnswerForm(QuestionForm questionForm, AppUser appUser) {
    this.questionForm = questionForm;
    this.appUser = appUser;
  }

  public AnswerForm(QuestionForm questionForm) {
    this.questionForm = questionForm;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public QuestionForm getQuestionForm() {
    return questionForm;
  }

  public void setQuestionForm(QuestionForm questionForm) {
    this.questionForm = questionForm;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public AppUser getAppUser() {
    return appUser;
  }

  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}


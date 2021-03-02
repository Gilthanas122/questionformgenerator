package com.bottomupquestionphd.demo.domains.daos.answers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answerForms")
public class AnswerForm {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private QuestionForm questionForm;

  @OneToMany(mappedBy = "answerForm", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private List<Answer> answers = new ArrayList<>();

  @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private AppUser appUser;

  public AnswerForm(){}

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
}


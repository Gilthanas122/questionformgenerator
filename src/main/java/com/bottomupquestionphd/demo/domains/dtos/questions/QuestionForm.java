package com.bottomupquestionphd.demo.domains.dtos.questions;

import com.bottomupquestionphd.demo.domains.dtos.AppUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class QuestionForm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private boolean finished;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private AppUser appUser;

  @OneToMany(mappedBy = "questionForm", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Question> questions = new ArrayList<>();

  public QuestionForm(){}

  public QuestionForm(String name) {
    this.name = name;
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
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }
}

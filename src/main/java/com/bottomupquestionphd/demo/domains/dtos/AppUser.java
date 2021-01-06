package com.bottomupquestionphd.demo.domains.dtos;

import com.bottomupquestionphd.demo.domains.dtos.questions.QuestionForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String userName;
  private String password;
  private boolean active;
  private boolean disabled;

  @OneToMany(mappedBy = "appUser")
  private List<QuestionForm> questionForms = new ArrayList<>();

  public AppUser(){}

  public AppUser(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public List<QuestionForm> getQuestionForms() {
    return questionForms;
  }

  public void setQuestionForms(List<QuestionForm> questionForms) {
    this.questionForms = questionForms;
  }
}

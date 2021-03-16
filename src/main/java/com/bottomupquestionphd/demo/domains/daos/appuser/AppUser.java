package com.bottomupquestionphd.demo.domains.daos.appuser;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String username;

  private String password;

  private boolean active = true;
  private String roles = "ROLE_USER";
  private boolean disabled;

  @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<QuestionForm> questionForms = new ArrayList<>();

  @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<AnswerForm> answerForms = new ArrayList<>();

  public AppUser() {
  }

  public AppUser(String username) {
    this.username = username;
  }

  public AppUser(String username, String password, String roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public AppUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRoles() {
    return roles;
  }

  public void addNewRole(String newRole) {
    this.roles += "," + newRole;
  }

  public void setRoles(String newRole) {
    this.roles = newRole;
  }

  public List<String> getRoleList() {
    if (this.roles.length() > 0) {
      return Arrays.asList(this.roles.split(","));
    }
    return new ArrayList<>();
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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<AnswerForm> getAnswerForms() {
    return answerForms;
  }

  public void setAnswerForms(List<AnswerForm> answerForms) {
    this.answerForms = answerForms;
  }

  public void addOneAnswerForm(AnswerForm answerForm) {
    this.answerForms.add(answerForm);
  }

  public boolean hasAnswerForm(long answerFormId) {
    return answerForms
            .stream()
            .anyMatch(form -> form.getId() == answerFormId);
  }
}

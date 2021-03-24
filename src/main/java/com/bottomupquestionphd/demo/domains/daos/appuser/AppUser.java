package com.bottomupquestionphd.demo.domains.daos.appuser;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Where(clause="disabled=0")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private String emailId;

  private boolean active = true;
  @NotNull
  private String roles = "ROLE_USER";
  private boolean disabled;

  @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<QuestionForm> questionForms = new ArrayList<>();

  @OneToMany(mappedBy = "appUser", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<AnswerForm> answerForms = new ArrayList<>();

  public static class Builder{
    private long id;
    private String username;
    private String password;
    private String emailId;
    private boolean active;
    private String roles = "ROLE_USER";
    private boolean disabled;
    private List<QuestionForm> questionForms = new ArrayList<>();
    private List <AnswerForm> answerForms = new ArrayList<>();

    public Builder id(long id){
      this.id = id;
      return this;
    }

    public Builder username(String username){
      this.username = username;
      return this;
    }

    public Builder password(String password){
      this.password = password;
      return this;
    }

    public Builder emailId(String emailId){
      this.emailId = emailId;
      return this;
    }

    public Builder active(boolean active){
      this.active = active;
      return  this;
    }

    public Builder roles(String roles){
      this.roles +="," + roles;
      return this;
    }

    public Builder disabled(boolean disabled){
      this.disabled = disabled;
      return this;
    }

    public Builder questionForms(List<QuestionForm> questionForms){
      this.questionForms = questionForms;
      return this;
    }

    public Builder answerForms(List<AnswerForm> answerForms){
      this.answerForms = answerForms;
      return this;
    }

    public AppUser build(){
      AppUser appUser = new AppUser();
      appUser.setId(this.id);
      appUser.setUsername(this.username);
      appUser.setPassword(this.password);
      appUser.setEmailId(this.emailId);
      appUser.setActive(this.active);
      appUser.setRoles(this.roles);
      appUser.setDisabled(this.disabled);
      appUser.setQuestionForms(this.questionForms);
      appUser.setAnswerForms(this.answerForms);
      return appUser;
    }
  }

  public AppUser() {
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

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }
}

package com.bottomupquestionphd.demo.domains.dtos.questionform;

public class QuestionFormCreateDTO {
  private long id;
  private String name;
  private String description;

  public QuestionFormCreateDTO() {
  }

  public QuestionFormCreateDTO(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public QuestionFormCreateDTO(long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}

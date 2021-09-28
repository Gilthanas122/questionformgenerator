package com.bottomupquestionphd.demo.domains.daos.questions;

public enum QuestionType {
  TEXTQUESTION ("TextQuestion"),
  CHECKBOXQUESTION ("CheckBoxQuestion"),
  RADIOBUTTONQUESTION ("RadioButtonQuestion"),
  SCALEQUESTION ("ScaleQuestion"),
  MULTIPLEANSWERQUESTION ("MultipleAnswerQuestion");

  private final String name;

  private QuestionType(String s) {
    name = s;
  }

  public boolean equalsName(String otherName) {
    // (otherName == null) check is not needed because name.equals(null) returns false
    return name.equals(otherName);
  }

  public String toString() {
    return this.name;
  }
}

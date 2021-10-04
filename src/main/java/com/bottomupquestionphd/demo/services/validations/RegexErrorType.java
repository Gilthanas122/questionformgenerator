package com.bottomupquestionphd.demo.services.validations;

public enum RegexErrorType {
  REGEXEMAIL ("email"),
  REGEXPASSWORD ("password");

  private final String name;

  private RegexErrorType(String s) {
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

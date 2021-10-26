package com.bottomupquestionphd.demo.exceptions.appuser;

public class PasswordNotComplexEnoughException extends Exception {

  public PasswordNotComplexEnoughException() {
  }

  public PasswordNotComplexEnoughException(String message) {
    super(message);
  }
}

package com.bottomupquestionphd.demo.exceptions.appuser;

public class UsernameAlreadyTakenException extends Exception {

  public UsernameAlreadyTakenException() {
  }

  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}

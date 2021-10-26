package com.bottomupquestionphd.demo.exceptions.appuser;

public class UserAlreadyDisabledException extends Exception {
  public UserAlreadyDisabledException() {
  }

  public UserAlreadyDisabledException(String message) {
    super(message);
  }
}

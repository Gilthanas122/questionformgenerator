package com.bottomupquestionphd.demo.exceptions.appuser;

public class BelongToAnotherUserException extends Exception {
  public BelongToAnotherUserException() {
  }

  public BelongToAnotherUserException(String message) {
    super(message);
  }

}

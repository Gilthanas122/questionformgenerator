package com.bottomupquestionphd.demo.exceptions.appuser;

public class InvalidChangePasswordException extends Exception{
  public InvalidChangePasswordException() {
  }

  public InvalidChangePasswordException(String message) {
    super(message);
  }
}

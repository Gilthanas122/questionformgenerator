package com.bottomupquestionphd.demo.exceptions.appuser;

public class NoSuchUserByEmailException extends Exception{
  public NoSuchUserByEmailException() {
  }

  public NoSuchUserByEmailException(String message) {
    super(message);
  }
}

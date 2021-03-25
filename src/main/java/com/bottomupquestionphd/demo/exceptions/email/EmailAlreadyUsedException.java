package com.bottomupquestionphd.demo.exceptions.email;

public class EmailAlreadyUsedException extends Exception{
  public EmailAlreadyUsedException() {
  }

  public EmailAlreadyUsedException(String message) {
    super(message);
  }
}

package com.bottomupquestionphd.demo.exceptions.email;

public class EmailAlreadyUserException extends Exception{
  public EmailAlreadyUserException() {
  }

  public EmailAlreadyUserException(String message) {
    super(message);
  }
}

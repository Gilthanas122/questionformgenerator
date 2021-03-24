package com.bottomupquestionphd.demo.exceptions.email;

public class ConfirmationTokenDoesNotExistException extends Exception{

  public ConfirmationTokenDoesNotExistException() {
  }

  public ConfirmationTokenDoesNotExistException(String message) {
    super(message);
  }
}

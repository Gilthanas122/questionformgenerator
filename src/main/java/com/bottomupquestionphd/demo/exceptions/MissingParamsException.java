package com.bottomupquestionphd.demo.exceptions;

public class MissingParamsException extends Exception {

  public MissingParamsException() {
  }

  public MissingParamsException(String message) {
    super(message);
  }
}

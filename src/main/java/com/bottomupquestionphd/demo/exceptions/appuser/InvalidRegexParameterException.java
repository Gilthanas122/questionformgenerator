package com.bottomupquestionphd.demo.exceptions.appuser;

public class InvalidRegexParameterException extends Exception{
  public InvalidRegexParameterException() {
  }

  public InvalidRegexParameterException(String message) {
    super(message);
  }
}

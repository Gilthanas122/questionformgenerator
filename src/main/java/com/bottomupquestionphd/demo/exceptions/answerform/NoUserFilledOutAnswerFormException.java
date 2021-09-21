package com.bottomupquestionphd.demo.exceptions.answerform;

public class NoUserFilledOutAnswerFormException extends Exception{
  public NoUserFilledOutAnswerFormException() {
  }

  public NoUserFilledOutAnswerFormException(String message) {
    super(message);
  }
}

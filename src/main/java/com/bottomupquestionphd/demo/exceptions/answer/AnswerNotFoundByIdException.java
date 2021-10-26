package com.bottomupquestionphd.demo.exceptions.answer;

public class AnswerNotFoundByIdException extends Exception {
  public AnswerNotFoundByIdException() {
  }

  public AnswerNotFoundByIdException(String message) {
    super(message);
  }
}

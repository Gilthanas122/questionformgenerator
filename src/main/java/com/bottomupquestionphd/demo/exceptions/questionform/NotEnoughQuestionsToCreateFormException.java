package com.bottomupquestionphd.demo.exceptions.questionform;

public class NotEnoughQuestionsToCreateFormException extends Exception {

  public NotEnoughQuestionsToCreateFormException() {
  }

  public NotEnoughQuestionsToCreateFormException(String message) {
    super(message);
  }
}

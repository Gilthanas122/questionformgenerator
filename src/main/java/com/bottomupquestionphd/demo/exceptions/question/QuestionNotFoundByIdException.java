package com.bottomupquestionphd.demo.exceptions.question;

public class QuestionNotFoundByIdException extends Exception {

  public QuestionNotFoundByIdException() {
  }

  public QuestionNotFoundByIdException(String message) {
    super(message);
  }
}

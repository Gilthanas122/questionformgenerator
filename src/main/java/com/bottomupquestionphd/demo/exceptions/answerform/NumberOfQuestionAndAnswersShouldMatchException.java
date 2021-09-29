package com.bottomupquestionphd.demo.exceptions.answerform;

public class NumberOfQuestionAndAnswersShouldMatchException extends Exception{
  public NumberOfQuestionAndAnswersShouldMatchException() {
  }

  public NumberOfQuestionAndAnswersShouldMatchException(String message) {
    super(message);
  }
}

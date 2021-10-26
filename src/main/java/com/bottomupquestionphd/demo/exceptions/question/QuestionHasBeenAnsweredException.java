package com.bottomupquestionphd.demo.exceptions.question;

public class QuestionHasBeenAnsweredException extends Exception {
  public QuestionHasBeenAnsweredException() {
  }

  public QuestionHasBeenAnsweredException(String message) {
    super(message);
  }
}

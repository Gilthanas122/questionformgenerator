package com.bottomupquestionphd.demo.exceptions.answerform;

public class AnswerFormAlreadyFilledOutByCurrentUserException extends Exception{
    public AnswerFormAlreadyFilledOutByCurrentUserException() {
    }

    public AnswerFormAlreadyFilledOutByCurrentUserException(String message) {
        super(message);
    }
}

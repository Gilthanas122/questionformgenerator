package com.bottomupquestionphd.demo.exceptions.textanswervote;

public class NoActualAnswerTextsToVoteForException extends Exception{
  public NoActualAnswerTextsToVoteForException() {
  }

  public NoActualAnswerTextsToVoteForException(String message) {
    super(message);
  }
}

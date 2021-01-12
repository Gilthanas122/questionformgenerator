package com.bottomupquestionphd.demo.exceptions.questionform;

public class NoQuestionFormsInDatabaseException extends Exception{
  public NoQuestionFormsInDatabaseException(){}
  public NoQuestionFormsInDatabaseException(String message){
    super(message);
  }
}

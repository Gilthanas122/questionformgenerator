package com.bottomupquestionphd.demo.exceptions.questionform;

public class MissingUserException extends Exception{
  public MissingUserException(){}
  public MissingUserException(String message){
    super(message);
  }
}

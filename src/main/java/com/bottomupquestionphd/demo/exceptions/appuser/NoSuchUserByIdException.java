package com.bottomupquestionphd.demo.exceptions.appuser;

public class NoSuchUserByIdException extends Exception {

  public NoSuchUserByIdException(){}
  public NoSuchUserByIdException(String message){
    super(message);
  }
}

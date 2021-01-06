package com.bottomupquestionphd.demo.exceptions.appuser;

public class NoSuchUserNameException extends Exception{

  public NoSuchUserNameException(){}

  public NoSuchUserNameException(String message){
    super(message);
  }
}

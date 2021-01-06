package com.bottomupquestionphd.demo.exceptions.appuser;

public class AppUserPasswordMissMatchException extends Exception{

  public AppUserPasswordMissMatchException(){}

  public AppUserPasswordMissMatchException(String message){
    super(message);
  }
}

package com.bottomupquestionphd.demo.exceptions.appuser;

public class NoUsersInDatabaseException extends Exception {

  public NoUsersInDatabaseException(){}

  public NoUsersInDatabaseException(String message){
    super(message);
  }
}

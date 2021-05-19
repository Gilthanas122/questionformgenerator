package com.bottomupquestionphd.demo.exceptions.appuser;

public class AppUserIsAlreadyActivatedException extends Exception{
  public AppUserIsAlreadyActivatedException() {
  }
  public AppUserIsAlreadyActivatedException(String message) {
    super(message);
  }
}

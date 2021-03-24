package com.bottomupquestionphd.demo.services.appuser;

public class AppUserNotActivatedException extends Exception {
  public AppUserNotActivatedException() {
  }

  public AppUserNotActivatedException(String message) {
    super(message);
  }
}

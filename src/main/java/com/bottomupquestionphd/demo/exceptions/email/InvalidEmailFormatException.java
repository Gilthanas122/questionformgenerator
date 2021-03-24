package com.bottomupquestionphd.demo.exceptions.email;

public class InvalidEmailFormatException extends Exception{
  public InvalidEmailFormatException() {
  }

  public InvalidEmailFormatException(String message) {
    super(message);
  }
}

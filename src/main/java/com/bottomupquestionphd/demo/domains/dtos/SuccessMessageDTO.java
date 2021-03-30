package com.bottomupquestionphd.demo.domains.dtos;

public class SuccessMessageDTO {
  private String status;
  private String message;

  public SuccessMessageDTO() {
  }

  public SuccessMessageDTO(String status, String message) {
    this.status = status;
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

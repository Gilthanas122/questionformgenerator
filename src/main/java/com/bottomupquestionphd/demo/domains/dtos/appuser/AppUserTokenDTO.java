package com.bottomupquestionphd.demo.domains.dtos.appuser;

public class AppUserTokenDTO {
  private String status;
  private String token;

  public AppUserTokenDTO() {
  }

  public AppUserTokenDTO(String status, String token) {
    this.status = status;
    this.token = token;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}

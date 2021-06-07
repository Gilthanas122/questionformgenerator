package com.bottomupquestionphd.demo.domains.dtos.appuser;

public class ChangePasswordDTO {
  private String password1;
  private String password2;

  public ChangePasswordDTO() {
  }

  public ChangePasswordDTO(String password1, String password2) {
    this.password1 = password1;
    this.password2 = password2;
  }

  public String getPassword1() {
    return password1;
  }

  public void setPassword1(String password1) {
    this.password1 = password1;
  }

  public String getPassword2() {
    return password2;
  }

  public void setPassword2(String password2) {
    this.password2 = password2;
  }
}

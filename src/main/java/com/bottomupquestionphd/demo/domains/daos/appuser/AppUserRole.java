package com.bottomupquestionphd.demo.domains.daos.appuser;

public enum AppUserRole {
  ADMIN("ROLE_ADMIN"),
  TEACHER("ROLE_TEACHER"),
  USER("ROLE_USER");

  private final String name;

  AppUserRole(String s) {
    name = s;
  }

  public boolean equalsName(String otherName) {
    // (otherName == null) check is not needed because name.equals(null) returns false
    return name.equals(otherName);
  }

  public String toString() {
    return this.name;
  }
}

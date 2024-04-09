package com.example.chaching.user.domain;

import lombok.Getter;

@Getter
public enum UserRole {
  CUSTOMER(Authority.CUSTOMER),
  SELLER(Authority.SELLER),
  ADMIN(Authority.ADMIN);

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

  public static class Authority {

    public static final String CUSTOMER = "ROLE_CUSTOMER";
    public static final String SELLER = "ROLE_SELLER";
    public static final String ADMIN = "ROLE_ADMIN";
  }

}

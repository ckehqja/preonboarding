package com.sparta.preonboarding.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
  ROLE_USER("user");

  private final String authority;
}

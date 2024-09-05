package com.sparta.preonboarding.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseUserDto {

  private final String username;
  private final String nickname;
  private final UserRoleEnum authorityName;

  public ResponseUserDto(User savedUser) {
    username = savedUser.getUsername();
    nickname = savedUser.getNickname();
    authorityName = savedUser.getRole();
  }
}

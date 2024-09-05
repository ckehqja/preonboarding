package com.sparta.preonboarding.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestUserDto {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String nickname;

}

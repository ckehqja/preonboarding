package com.sparta.preonboarding.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {

  @Id @GeneratedValue
  private long id;

  private String username;

  private String password;

  private String nickname;

  @Enumerated(EnumType.STRING)
  private UserRoleEnum role;

  public User(String username, String nickname, String password) {
    this.username = username;
    this.nickname = nickname;
    this.password = password;
    role = UserRoleEnum.ROLE_USER;
  }
}

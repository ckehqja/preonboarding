package com.sparta.preonboarding.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {

  @Id @GeneratedValue
  public long id;

}

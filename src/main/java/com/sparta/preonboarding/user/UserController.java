package com.sparta.preonboarding.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseUserDto createUser(
      @Valid @RequestBody RequestUserDto requestUserDto) {
    return userService.createUser(requestUserDto);
  }

}

package com.sparta.preonboarding.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public ResponseUserDto createUser(RequestUserDto requestUserDto) {
    if (userRepository.existsByUsername(requestUserDto.getUsername())) {
      throw new IllegalArgumentException("중복된 유저이름입니다.");
    }

    String username = requestUserDto.getUsername();
    String nickname = requestUserDto.getNickname();
    String password = passwordEncoder.encode(requestUserDto.getPassword());

    User savedUser = userRepository.save(new User(username, nickname, password));
    return new ResponseUserDto(savedUser);
  }
}


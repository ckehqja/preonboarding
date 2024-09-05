package com.sparta.preonboarding.security;

import com.sparta.preonboarding.user.User;
import com.sparta.preonboarding.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override //loadUserByUser"name" 을 오버라이드 하여 사용자 "ID"로 사용자 정보를 가져옴
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 ID 입니다."));
        return new UserDetailsImpl(user);
    }
}
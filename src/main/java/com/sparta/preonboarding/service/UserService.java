package com.sparta.preonboarding.service;

import com.sparta.preonboarding.dto.RequestRefreshDto;
import com.sparta.preonboarding.dto.RequestUserDto;
import com.sparta.preonboarding.dto.ResponseRefreshDto;
import com.sparta.preonboarding.dto.ResponseUserDto;
import com.sparta.preonboarding.entity.RefreshToken;
import com.sparta.preonboarding.entity.User;
import com.sparta.preonboarding.jwt.JwtProvider;
import com.sparta.preonboarding.repository.RefreshTokenRepository;
import com.sparta.preonboarding.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

    public ResponseRefreshDto reissue(RequestRefreshDto requestRefreshDto, HttpServletRequest request) {

        log.info("reissue ==> refresh 토큰 통한 토큰 재발급 시작");
        log.info("get refreshtoken : " + requestRefreshDto.getRefreshToken());
        String refreshToken = jwtProvider.getTokenWithoutBearer(requestRefreshDto.getRefreshToken());


        if (!jwtProvider.validateToken(refreshToken)) {   //refresh 토큰이 유효기간이 지났는지 검증
            throw new IllegalArgumentException("재로그인 필요");
        }
        log.info("reissue ==> refresh 토큰 검증 성공");

        String username = jwtProvider.getUserInfoFromToken(refreshToken).getSubject();

        RefreshToken findRefreshToken = refreshTokenRepository.findByUsername(username)    //DB에 실제로 그 유저에게 발급된 refresh토큰이 있는지 확인
                .orElseThrow(() -> new IllegalArgumentException("로그아웃된 사용자"));
        log.info("reissue ==> DB에 사용자 이름과 refresh 토큰 존재 확인");


        if (!findRefreshToken.getRefreshToken().equals(jwtProvider.BEARER_PREFIX + refreshToken)) {
            throw new IllegalArgumentException("DB의 refresh토큰과 일치하지 않음.");
        }
        log.info("reissue ==> DB refresh token과 일치 확인");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(username + " 은 없는 사용자 아이디입니다."));


        String newAccessToken = jwtProvider.createAccessToken(     //새로운 토큰 발급.
                username,
                user.getRole()
        );

        log.info("reissue ==> 새 토큰 발급 : " + newAccessToken);
        log.info("토큰 authorities : " + user.getRole());        //잘 되는 지 확인용. 나중엔 지워야 함.

        String newRefreshToken = jwtProvider.createRefreshToken(username);

        findRefreshToken.updateRefreshToken(newRefreshToken);    //refresh 토큰도 업데이트.
        refreshTokenRepository.save(findRefreshToken);

        ResponseRefreshDto responseRefreshDto = new ResponseRefreshDto(newAccessToken, newRefreshToken);

        return responseRefreshDto;
    }
}


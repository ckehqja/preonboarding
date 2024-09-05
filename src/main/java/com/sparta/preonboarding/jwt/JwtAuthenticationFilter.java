package com.sparta.preonboarding.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.preonboarding.security.UserDetailsImpl;
import com.sparta.preonboarding.user.LoginRequestDto;
import com.sparta.preonboarding.user.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성") // Spring Security의 인증 흐름에 따라 로그인 요청을 처리하는 필터
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtProvider jwtProvider;

  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
    setFilterProcessesUrl("/users/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequestDto requestDto = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getUsername(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("로그인 성공 및 JWT 생성");
    UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
    String username = userDetails.getUsername();
    UserRoleEnum role = userDetails.getUser().getRole();

    String accessToken = jwtProvider.createAccessToken(username, role);
    String refreshToken = jwtProvider.createRefreshToken(username);

    response.addHeader(JwtProvider.AUTHORIZATION_HEADER, accessToken);
    response.addHeader("RefreshToken", refreshToken);

    // JSON 형식으로 로그인 성공 메시지 작성
    String loginSuccessMessage = String.format("{\"accessToken\" : \"%s\"}", accessToken);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(loginSuccessMessage);
  }


  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json");

    String result = "{" +
        "\"status\": 401," +
        "\"message\": \"" + failed.getMessage() + "\"" +
        "}";

    response.getWriter().write(result);
  }
}
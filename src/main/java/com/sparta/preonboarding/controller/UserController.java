package com.sparta.preonboarding.controller;

import com.sparta.preonboarding.dto.RequestRefreshDto;
import com.sparta.preonboarding.dto.RequestUserDto;
import com.sparta.preonboarding.dto.ResponseRefreshDto;
import com.sparta.preonboarding.dto.ResponseUserDto;
import com.sparta.preonboarding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseUserDto createUser(
          @Valid @RequestBody RequestUserDto requestUserDto) {
    return userService.createUser(requestUserDto);
  }

  @PostMapping("/reissue")
  @Operation(summary = "토큰 재발급", description = "refresh 토큰으로 access 토큰을 재발급합니다.", responses = {
          @ApiResponse(responseCode = "200", description = "재발급 성공", content = {
                  @Content(mediaType = "application/json", schema =
                  @Schema(implementation = ResponseRefreshDto.class))
          }),
          @ApiResponse(responseCode = "400", description = "재발급 실패")
  })
  public ResponseEntity<ResponseRefreshDto> reissue(@RequestBody RequestRefreshDto requestRefreshDto, HttpServletRequest request){
    log.info("SignController reissue ==> 토큰 재발급 메서드");
    ResponseRefreshDto responseRefreshDto = userService.reissue(requestRefreshDto, request);

    return ResponseEntity.status(HttpStatus.OK).body(responseRefreshDto);
  }
}

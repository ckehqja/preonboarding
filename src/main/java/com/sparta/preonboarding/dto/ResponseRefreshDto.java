package com.sparta.preonboarding.dto;

import lombok.Getter;

@Getter
public class ResponseRefreshDto {

    private  String newAcessToken;
    private String newRefreshToken;

    public ResponseRefreshDto(String newAccessToken, String newRefreshToken) {
        this.newAcessToken = newAccessToken;
        this.newRefreshToken = newRefreshToken;
    }
}

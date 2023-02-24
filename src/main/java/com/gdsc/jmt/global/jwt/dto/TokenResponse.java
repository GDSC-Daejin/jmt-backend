package com.gdsc.jmt.global.jwt.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}

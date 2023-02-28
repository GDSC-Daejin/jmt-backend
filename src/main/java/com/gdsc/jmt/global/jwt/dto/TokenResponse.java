package com.gdsc.jmt.global.jwt.dto;


public record TokenResponse(
         String grantType,
         String accessToken,
         String refreshToken,
         Long accessTokenExpiresIn)
{ }

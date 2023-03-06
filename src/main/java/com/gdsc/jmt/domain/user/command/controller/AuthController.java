package com.gdsc.jmt.domain.user.command.controller;

import com.gdsc.jmt.domain.user.command.dto.SocialLoginRequest;
import com.gdsc.jmt.domain.user.command.service.AppleService;
import com.gdsc.jmt.domain.user.command.service.AuthService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.ApiResponse;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.messege.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FirstVersionRestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AppleService appleService;

    @PostMapping("/auth/google")
    public ApiResponse<TokenResponse> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest.token());
        return ApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/auth/apple")
    public ApiResponse<TokenResponse> appleLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        TokenResponse tokenResponse = appleService.appleLogin(socialLoginRequest.token());
        return ApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }
}

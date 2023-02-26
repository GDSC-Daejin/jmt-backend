package com.gdsc.jmt.domain.user.command.controller;

import com.gdsc.jmt.domain.user.command.dto.SocialLoginRequest;
import com.gdsc.jmt.domain.user.command.service.AuthService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.ApiResponse;
import com.gdsc.jmt.global.messege.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FirstVersionRestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/google")
    public ApiResponse<?> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        authService.googleLogin(socialLoginRequest.token());

        return ApiResponse.createResponseWithMessage(null, UserMessage.LOGIN_SUCCESS);
    }
}

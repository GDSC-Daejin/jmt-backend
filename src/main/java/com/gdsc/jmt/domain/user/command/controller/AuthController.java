package com.gdsc.jmt.domain.user.command.controller;

import com.gdsc.jmt.domain.user.command.controller.springdocs.*;
import com.gdsc.jmt.domain.user.command.dto.AndroidAppleLoginRequest;
import com.gdsc.jmt.domain.user.command.dto.LogoutRequest;
import com.gdsc.jmt.domain.user.command.dto.SocialLoginRequest;
import com.gdsc.jmt.domain.user.command.service.AuthService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.UserMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "사용자 인증 관련 컨트롤러")
@FirstVersionRestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/google")
    @GoogleLoginSpringDocs
    public JMTApiResponse<TokenResponse> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest.token());
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/auth/android/apple")
    @AndroidAppleLoginSpringDocs
    public JMTApiResponse<TokenResponse> appleLoginFromAndroid(@RequestBody AndroidAppleLoginRequest androidAppleLoginRequest) {
        TokenResponse tokenResponse = authService.appleLoginFromAndroid(androidAppleLoginRequest);
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/auth/apple")
    @AppleLoginSpringDocs
    public JMTApiResponse<TokenResponse> appleLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        TokenResponse tokenResponse = authService.appleLogin(socialLoginRequest.token());
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/auth/test")
    public JMTApiResponse<TokenResponse> loginForTest() {
        TokenResponse tokenResponse = authService.loginForTest();
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/token")
    @ReissueSpringDocs
    public JMTApiResponse<TokenResponse> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) {
        TokenResponse tokenResponse = authService.reissue(user.getEmail(), user.getAggreagatedId(),logoutRequest.refreshToken());
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.REISSUE_SUCCESS);
    }

    @DeleteMapping("/user")
    @LogoutSpringDocs
    public JMTApiResponse<?> logout(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) {
        authService.logout(user.getEmail() , logoutRequest.refreshToken());
        return JMTApiResponse.createResponseWithMessage(null, UserMessage.LOGOUT_SUCCESS);
    }


    @PostMapping("/user/createAccessToken/{userAggregateId}")
    public JMTApiResponse<?> createAccessToken(@RequestParam String userAggregateId) {
        String accessToken = authService.createAccessToken(userAggregateId);
        return JMTApiResponse.createResponseWithMessage(accessToken, UserMessage.LOGIN_SUCCESS);
    }
}

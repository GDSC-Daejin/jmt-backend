package com.gdsc.jmt.domain.user.command.controller;

import com.gdsc.jmt.domain.user.command.controller.springdocs.GoogleLoginSpringDocs;
import com.gdsc.jmt.domain.user.command.controller.springdocs.LogoutSpringDocs;
import com.gdsc.jmt.domain.user.command.controller.springdocs.ReissueSpringDocs;
import com.gdsc.jmt.domain.user.command.dto.LogoutRequest;
import com.gdsc.jmt.domain.user.command.dto.SocialLoginRequest;
import com.gdsc.jmt.domain.user.command.service.AuthService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.messege.UserMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/token")
    @ReissueSpringDocs
    public JMTApiResponse<TokenResponse> reissue(@AuthenticationPrincipal User user, @RequestBody LogoutRequest logoutRequest) {
        TokenResponse tokenResponse = authService.reissue(user.getUsername(), logoutRequest.refreshToken());
        return JMTApiResponse.createResponseWithMessage(tokenResponse, UserMessage.REISSUE_SUCCESS);
    }

    @DeleteMapping("/user")
    @LogoutSpringDocs
    public JMTApiResponse<?> logout(@AuthenticationPrincipal User user, @RequestBody LogoutRequest logoutRequest) {
        authService.logout(user.getUsername() , logoutRequest.refreshToken());
        return JMTApiResponse.createResponseWithMessage(null, UserMessage.LOGOUT_SUCCESS);
    }
}

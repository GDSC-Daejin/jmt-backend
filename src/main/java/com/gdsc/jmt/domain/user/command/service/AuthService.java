package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.GoogleLoginCommand;
import com.gdsc.jmt.domain.user.command.PersistRefreshTokenCommand;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.TokenProvider;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.messege.AuthMessage;
import com.gdsc.jmt.global.messege.DefaultMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    private final TokenProvider tokenProvider;
    private final CommandGateway commandGateway;

    @Transactional
    public TokenResponse googleLogin(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.INVALID_TOKEN);
            } else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                // 실제 유저 생성
                CompletableFuture<UserEntity> userResult = commandGateway.send(new GoogleLoginCommand(
                        UUID.randomUUID().toString(),
                        userInfo
                ));

                // JWT 발급 및 Response 반환
                TokenResponse tokenResponse = createToken(userResult.get());

                // JWT 발급 받고 Response 쏘는게 가장 문제인듯
                commandGateway.send(new PersistRefreshTokenCommand(
                        UUID.randomUUID().toString(),
                        userResult.get().getId(),
                        tokenResponse.refreshToken()
                ));

                return tokenResponse;
            }
        } catch (IllegalArgumentException | HttpClientErrorException | GeneralSecurityException | IOException e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private TokenResponse createToken(UserEntity user) {
        return tokenProvider.generateJwtToken(user.getId().toString(), RoleType.MEMBER);
    }
}

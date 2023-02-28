package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.GoogleLoginCommand;
import com.gdsc.jmt.domain.user.command.PersistRefreshTokenCommand;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.TokenProvider;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.messege.AuthMessage;
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
//                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.INVALID_TOKEN);
            } else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                commandGateway.sendAndWait(new GoogleLoginCommand(
                        UUID.randomUUID().toString(),
                        userInfo
                ));

                // TODO : JWT Payload를 애플과 공통된 email 혹은 sub로 해야함.
                // 왜 다양한 서비스들이 회원가입할때 아이디를 이메일로 쓰는지 알겠음
                TokenResponse tokenResponse = createToken(userInfo.getEmail());
                commandGateway.sendAndWait(new PersistRefreshTokenCommand(
                        UUID.randomUUID().toString(),
                        userInfo.getEmail(),
                        tokenResponse.refreshToken()
                ));

                return tokenResponse;
            }
        } catch (IllegalArgumentException | HttpClientErrorException | GeneralSecurityException | IOException e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    private TokenResponse createToken(String email) {
        return tokenProvider.generateJwtToken(email, RoleType.MEMBER);
    }
}

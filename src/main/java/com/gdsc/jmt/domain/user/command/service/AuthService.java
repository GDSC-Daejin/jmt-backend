package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.GoogleLoginCommand;
import com.gdsc.jmt.domain.user.command.LogoutCommand;
import com.gdsc.jmt.domain.user.command.PersistRefreshTokenCommand;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.TokenProvider;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.messege.AuthMessage;
import com.gdsc.jmt.global.messege.UserMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

                String userAggregateId = UUID.randomUUID().toString();
                commandGateway.sendAndWait(new GoogleLoginCommand(
                        userAggregateId,
                        userInfo
                ));

                String refreshTokenAggregateId = UUID.randomUUID().toString();
                TokenResponse tokenResponse = createToken(userAggregateId, refreshTokenAggregateId);
                commandGateway.sendAndWait(new PersistRefreshTokenCommand(
                        refreshTokenAggregateId,
                        userInfo.getEmail(),
                        tokenResponse.refreshToken()
                ));

                return tokenResponse;
            }
        } catch (IllegalArgumentException | HttpClientErrorException | GeneralSecurityException | IOException e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    public void logout(String email, String refreshToken) {
        try {
            if(tokenProvider.validateToken(refreshToken)) {
                Claims claims = tokenProvider.parseClaims(refreshToken);

                commandGateway.sendAndWait(new LogoutCommand(
                        claims.getSubject(),
                        email,
                        refreshToken)
                );
            }
            else
                throw new ApiException(UserMessage.LOGOUT_FAIL);
        }
        catch (Exception e) {
            throw new ApiException(UserMessage.LOGOUT_FAIL);
        }
    }

    private TokenResponse createToken(String userAggregateId, String refreshTokenAggregateId) {
        return tokenProvider.generateJwtToken(userAggregateId, refreshTokenAggregateId, RoleType.MEMBER);
    }
}

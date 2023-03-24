package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.SignUpCommand;
import com.gdsc.jmt.domain.user.command.LogoutCommand;
import com.gdsc.jmt.domain.user.command.PersistRefreshTokenCommand;
import com.gdsc.jmt.domain.user.command.dto.AndroidAppleLoginRequest;
import com.gdsc.jmt.domain.user.command.info.Reissue;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.common.SocialType;
import com.gdsc.jmt.domain.user.oauth.info.OAuth2UserInfo;
import com.gdsc.jmt.domain.user.oauth.info.impl.AppleOAuth2UserInfo;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.domain.user.apple.AppleUtil;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${apple.side.google.client.id}")
    private String appleSideGoogleClientId;
    private final TokenProvider tokenProvider;
    private final CommandGateway commandGateway;

    // 인증 로직만 CQRS 예외
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse googleLogin(String idToken) {
        // TODO : GoogleIdTokenVerifier는 Bean으로 등록하고 써도 될듯???
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(googleClientId, appleSideGoogleClientId))
                .build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.INVALID_TOKEN);
            }
            else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());
                String userAggregateId = sendSignUpCommand(userInfo, SocialType.GOOGLE);
                return sendGenerateJwtTokenCommend(userInfo.getEmail(), userAggregateId);
            }
        } catch (IllegalArgumentException | HttpClientErrorException | GeneralSecurityException | IOException e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    public TokenResponse appleLoginFromAndroid(AndroidAppleLoginRequest androidAppleLoginRequest) {
        if(!appleSideGoogleClientId.equals(androidAppleLoginRequest.clientId())) {
            throw new ApiException(AuthMessage.LOGIN_BAD_REQUEST);
        }

        // TODO : 안드로이드에서 애플 로그인 Request는 현재 SUB가 존재하지 않음 (애초에 SUB 사용안하고 있음 지금)
        OAuth2UserInfo userInfo = new AppleOAuth2UserInfo("이 sub는 없습니다", androidAppleLoginRequest.email());
        String userAggregateId = sendSignUpCommand(userInfo, SocialType.APPLE);
        return sendGenerateJwtTokenCommend(androidAppleLoginRequest.email(), userAggregateId);
    }

    @Transactional
    public TokenResponse appleLogin(String idToken) {
        OAuth2UserInfo userInfo = AppleUtil.getAppleUserInfo(idToken);

        String userAggregateId = sendSignUpCommand(userInfo, SocialType.APPLE);
        return sendGenerateJwtTokenCommend(userInfo.getEmail(), userAggregateId);
    }

    @Transactional
    public TokenResponse reissue(String email, String userAggregateId, String refreshToken) {
        validateRefreshToken(refreshToken);

        String refreshTokenAggregateId = UUID.randomUUID().toString();
        TokenResponse tokenResponse = createToken(email, userAggregateId, refreshTokenAggregateId);
        Reissue reissue = new Reissue(true, refreshToken, tokenResponse.refreshToken());
        commandGateway.sendAndWait(new PersistRefreshTokenCommand(
                refreshTokenAggregateId,
                email,
                null,
                reissue
        ));

        return tokenResponse;
    }

    @Transactional
    public void logout(String email, String refreshToken) {
        validateRefreshToken(refreshToken);

        Claims claims = tokenProvider.parseClaims(refreshToken);
        commandGateway.sendAndWait(new LogoutCommand(
                claims.getSubject(),
                email,
                refreshToken)
        );
    }

    private String sendSignUpCommand(OAuth2UserInfo userInfo, SocialType socialType) {
        String userAggregateId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new SignUpCommand(
                userAggregateId,
                userInfo,
                socialType));

        Optional<UserEntity> user = userRepository.findByEmail(userInfo.getEmail());
        if(user.isPresent())
            userAggregateId = user.get().getUserAggregateId();
        return userAggregateId;
    }

    private TokenResponse sendGenerateJwtTokenCommend(String email, String userAggregateId) {
        String refreshTokenAggregateId = UUID.randomUUID().toString();
        TokenResponse tokenResponse = createToken(email, userAggregateId, refreshTokenAggregateId);
        commandGateway.sendAndWait(new PersistRefreshTokenCommand(
                refreshTokenAggregateId,
                email,
                tokenResponse.refreshToken(),
                null
        ));
        return tokenResponse;
    }

    private void validateRefreshToken(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken))
            throw new ApiException(UserMessage.REFRESH_TOKEN_INVALID);
    }

    private TokenResponse createToken(String email, String userAggregateId, String refreshTokenAggregateId) {
        return tokenProvider.generateJwtToken(email, userAggregateId, refreshTokenAggregateId, RoleType.MEMBER);
    }
}
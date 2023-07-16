package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.dto.AndroidAppleLoginRequest;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.oauth.info.OAuth2UserInfo;
import com.gdsc.jmt.domain.user.oauth.info.impl.AppleOAuth2UserInfo;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.domain.user.apple.AppleUtil;
import com.gdsc.jmt.domain.user.query.entity.RefreshTokenEntity;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.RefreshTokenRepository;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.TokenProvider;
import com.gdsc.jmt.global.jwt.dto.TokenResponse;
import com.gdsc.jmt.global.jwt.dto.UserLoginAction;
import com.gdsc.jmt.global.messege.AuthMessage;
import com.gdsc.jmt.global.messege.UserMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${apple.side.google.client.id}")
    private String appleSideGoogleClientId;
    private final TokenProvider tokenProvider;

    // 인증 로직만 CQRS 예외
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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

                UserLoginAction action = signUpOrSignIn(userInfo.createUserEntity());
                TokenResponse tokenResponse = sendGenerateJwtTokenCommend(userInfo.getEmail());
                tokenResponse.updateLoginActionFlag(action);
                return tokenResponse;
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
        UserLoginAction action = signUpOrSignIn(userInfo.createUserEntity());
        TokenResponse tokenResponse = sendGenerateJwtTokenCommend(androidAppleLoginRequest.email());
        tokenResponse.updateLoginActionFlag(action);
        return tokenResponse;
    }

    @Transactional
    public TokenResponse appleLogin(String idToken) {
        OAuth2UserInfo userInfo = AppleUtil.getAppleUserInfo(idToken);
        UserLoginAction action = signUpOrSignIn(userInfo.createUserEntity());
        TokenResponse tokenResponse = sendGenerateJwtTokenCommend(userInfo.getEmail());
        tokenResponse.updateLoginActionFlag(action);
        return tokenResponse;
    }

    @Transactional
    public TokenResponse loginForTest() {
        OAuth2UserInfo userInfo = new AppleOAuth2UserInfo("test", "test@naver.com");
        UserLoginAction action = signUpOrSignIn(userInfo.createUserEntity());
        TokenResponse tokenResponse = sendGenerateJwtTokenCommend(userInfo.getEmail());
        tokenResponse.updateLoginActionFlag(action);
        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissue(String email, String refreshToken) {
        validateRefreshToken(refreshToken);
        return sendGenerateJwtTokenCommend(email);
    }

    @Transactional
    public void logout(String email, String refreshToken) {
        validateRefreshToken(refreshToken);
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshTokenByEmail(email);
        if(refreshTokenEntity == null || !refreshToken.equals(refreshTokenEntity.getRefreshToken()))
            throw new ApiException(UserMessage.REFRESH_TOKEN_INVALID);
        refreshTokenRepository.delete(refreshTokenEntity);
    }

    private TokenResponse sendGenerateJwtTokenCommend(String email) {
        TokenResponse tokenResponse = createToken(email);

        UserEntity userEntity = checkExistingUserByUserEmail(email);
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshToken(userEntity.getId());

        if(refreshTokenEntity == null) {
            RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity(
                    userEntity.getId(),
                    tokenResponse.refreshToken()
            );
            refreshTokenRepository.save(newRefreshTokenEntity);
        }
        else {
            refreshTokenEntity.setRefreshToken(tokenResponse.refreshToken());
        }

        return tokenResponse;
    }

    private RefreshTokenEntity checkExistingRefreshTokenByEmail(String email) {
        UserEntity userEntity = checkExistingUserByUserEmail(email);
        return checkExistingRefreshToken(userEntity.getId());
    }

    private UserEntity checkExistingUserByUserEmail(String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        if(result.isPresent())
            return result.get();
        else
            throw new ApiException(UserMessage.USER_NOT_FOUND);
    }

    private RefreshTokenEntity checkExistingRefreshToken(Long userId) {
        Optional<RefreshTokenEntity> result = refreshTokenRepository.findByUserId(userId);
        return result.orElse(null);
    }

    private void validateRefreshToken(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken))
            throw new ApiException(UserMessage.REFRESH_TOKEN_INVALID);
    }

    private TokenResponse createToken(String email) {
        return tokenProvider.generateJwtToken(email, RoleType.MEMBER);
    }

    private UserLoginAction signUpOrSignIn(UserEntity user) {
        if(user == null)
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        UserLoginAction userLoginAction = UserLoginAction.LOG_IN;
        Optional<UserEntity> origin = userRepository.findByEmail(user.getEmail());
        origin.ifPresent(
                userEntity ->  {
                    user.setId(userEntity.getId());
                    user.setNickname(userEntity.getNickname());
                    user.setProfileImageUrl(userEntity.getProfileImageUrl());
                }
        );
        if (origin.isEmpty()){
            userLoginAction = UserLoginAction.SIGN_UP;
        }
        userRepository.save(user);
        return userLoginAction;
    }
}
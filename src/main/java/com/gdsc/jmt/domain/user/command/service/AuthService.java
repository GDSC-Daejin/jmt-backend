package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.GoogleLoginCommand;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.global.exception.ApiException;
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
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${google.client.id}")
    private final String googleClientId;

    private final CommandGateway commandGateway;

    @Transactional
    public CompletableFuture<String> googleLogin(String idToken) {
        // 1. 구글 사용자 정보 가져오기 (얘는 Service에서 해야 할듯?) 완료.
        // 2. 사용자 생성 (Command 실행 시키면 될듯?)
        // 3. 성공 or 실패 Response 보내기 (?????)
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.INVALID_TOKEN);
            } else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                // 이 부분 Command로 Send 하기
                CompletableFuture<String> test = commandGateway.send(new GoogleLoginCommand(
                        UUID.randomUUID().toString(),
                        userInfo
                ));

                // JWT 발급 및 Response 반환
                return createToken(user);
            }
        } catch (HttpClientErrorException | GeneralSecurityException | IOException e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }

        return commandGateway.send();
    }
}

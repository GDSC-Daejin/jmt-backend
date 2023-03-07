package com.gdsc.jmt.command;

import com.gdsc.jmt.domain.user.command.SignUpCommand;
import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import com.gdsc.jmt.domain.user.command.event.CreateUserEvent;
import com.gdsc.jmt.domain.user.common.SocialType;
import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("dev")
public class SignUpCommandTest {
    private FixtureConfiguration<UserAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(UserAggregate.class);
    }

    @Test
    public void 구글_로그인_COMMAND_테스트() {
        // given
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject("testId");
        payload.setEmail("test");
        payload.set("name", "testName");
        payload.set("picture", "testImage");
        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(payload);
        String uuid = UUID.randomUUID().toString();

        fixture.given()
                .when(new SignUpCommand(uuid, userInfo, SocialType.GOOGLE))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new CreateUserEvent(uuid, userInfo.getEmail(), SocialType.GOOGLE));
    }
}

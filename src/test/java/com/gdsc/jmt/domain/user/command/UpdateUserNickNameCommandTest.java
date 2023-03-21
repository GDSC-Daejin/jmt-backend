package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import lombok.RequiredArgsConstructor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("dev")
class UpdateUserNickNameCommandTest {

    private FixtureConfiguration<UserAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(UserAggregate.class);
    }

    @Test
    @WithUserDetails(value = "example2@google.com")
    public void 유저_닉네임_등록_COMMAND_TEST() {
        //given

        //when

        //then

    }
}
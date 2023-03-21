package com.gdsc.jmt.domain.user.command.dto;

public record NicknameRequest(
        String userAggregateId,
        String nickname
) {
}

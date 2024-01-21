package com.gdsc.jmt.domain.user.command.dto;

public record LogoutRequest(String accessToken, String refreshToken) {
}

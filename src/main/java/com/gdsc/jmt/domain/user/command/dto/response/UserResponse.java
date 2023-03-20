package com.gdsc.jmt.domain.user.command.dto.response;

import org.springframework.web.multipart.MultipartFile;

public record UserResponse(
        String email,
        String nickname
) {
}

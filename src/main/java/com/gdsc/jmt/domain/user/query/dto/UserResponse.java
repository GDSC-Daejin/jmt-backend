package com.gdsc.jmt.domain.user.query.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserResponse(
        String email,
        String nickname,
        String profileImg ) {
}

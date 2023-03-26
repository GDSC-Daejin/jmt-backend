package com.gdsc.jmt.global.messege;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessage implements ResponseMessage{
    USER_NOT_FOUND("해당 유저는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    LOGIN_CONFLICT("이미 로그인이 되어있습니다.", HttpStatus.CONFLICT),
    LOGIN_SUCCESS("로그인에 성공했습니다.", HttpStatus.OK),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다.", HttpStatus.OK),
    REISSUE_SUCCESS("토큰 재발급에 성공하였습니다.", HttpStatus.CREATED),
    REFRESH_TOKEN_INVALID("RefreshToken이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_UPDATE_SUCCESS("닉네임 변경에 성공하였습니다.", HttpStatus.OK),
    NICKNAME_IS_DUPLICATED("이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    NICKNAME_IS_AVAILABLE("사용 가능한 닉네임입니다.", HttpStatus.OK),
    PROFILE_IMAGE_UPDATE_SUCCESS("프로필 사진 등록에 성공하였습니다.", HttpStatus.OK),
    PROFILE_IMAGE_UPLOAD_FAIL("프로필 사진 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;
}

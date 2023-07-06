package com.gdsc.jmt.domain.user.command.controller;

import com.gdsc.jmt.domain.user.command.controller.springdocs.UpdateUserNicknameSpringDocs;
import com.gdsc.jmt.domain.user.command.controller.springdocs.UpdateUserProfileImgSpringDocs;
import com.gdsc.jmt.domain.user.command.dto.NicknameRequest;
import com.gdsc.jmt.domain.user.command.dto.ProfileImgRequest;
import com.gdsc.jmt.domain.user.command.dto.response.UserNicknameResponse;
import com.gdsc.jmt.domain.user.command.service.UserService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.UserMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 정보 관련 컨트롤러")
@FirstVersionRestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/nickname")
    @UpdateUserNicknameSpringDocs
    public JMTApiResponse<UserNicknameResponse> updateUserNickname(@AuthenticationPrincipal UserInfo user, @RequestBody NicknameRequest nicknameRequest) {
        userService.updateUserNickName(user.getAggreagatedId(), nicknameRequest.nickname());
        UserNicknameResponse response = new UserNicknameResponse(user.getEmail(), nicknameRequest.nickname());
        return JMTApiResponse.createResponseWithMessage(response, UserMessage.NICKNAME_UPDATE_SUCCESS);
    }

    @PostMapping(value = "/user/profileImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UpdateUserProfileImgSpringDocs
    public JMTApiResponse<?> updateUserProfileImg(@AuthenticationPrincipal UserInfo user,
                                                  @ModelAttribute ProfileImgRequest profileImgRequest) {
        String responseUrl = userService.updateUserProfileImg(user.getAggreagatedId(), profileImgRequest.profileImg());
        return JMTApiResponse.createResponseWithMessage(responseUrl, UserMessage.PROFILE_IMAGE_UPDATE_SUCCESS);
    }

}
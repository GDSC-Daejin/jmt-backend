package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.UpdateUserNickNameCommand;
import com.gdsc.jmt.domain.user.command.UpdateUserProfileImgCommand;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommandGateway commandGateway;
    private final S3FileService s3FileService;
    //TODO : 임시로 피오니 로컬로 테스트 해봤어요. 나중에는 s3로 바꿔야할 것 같아요.
    private final String DEFAULT_PROFILE_IMAGE_URL = "https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png";

    @Transactional
    public void updateUserNickName(String userAggregateId, String nickName) {
        commandGateway.send(new UpdateUserNickNameCommand(
                userAggregateId,
                nickName
        ));
    }

    @Transactional
    public String updateUserProfileImg(String userAggregateId, MultipartFile profileImg) {
        String responseUrl = "";
        if(profileImg != null) {
            try {
                responseUrl = s3FileService.upload(profileImg, "profileImg");
            } catch (IOException e) {
                throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
            }
        }
        if(profileImg == null) {
            responseUrl = DEFAULT_PROFILE_IMAGE_URL;
        }

        commandGateway.send(new UpdateUserProfileImgCommand(
                userAggregateId,
                responseUrl
        ));
        return responseUrl;
    }
}

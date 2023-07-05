package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.UpdateUserNickNameCommand;
import com.gdsc.jmt.domain.user.command.UpdateUserProfileImgCommand;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import java.io.IOException;
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

    private final String DEFAULT_PROFILE_IMAGE_URL = "https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png";

    @Transactional
    public void updateUserNickName(String userAggregateId, String nickName) {
        commandGateway.send(new UpdateUserNickNameCommand(
                userAggregateId,
                nickName
        ));
    }

    @Transactional
    public String updateUserProfileImg(String userAggregateId, MultipartFile profileImg, boolean upload) {
        String responseUrl = null;
        if(upload) {
            responseUrl = uploadProfileImage(profileImg);
            if(profileImg == null) {
                throw new ApiException(UserMessage.PROFILE_IMAGE_NOT_FOUND);
            }
        } else {
            responseUrl = DEFAULT_PROFILE_IMAGE_URL;
        }
        commandGateway.send(new UpdateUserProfileImgCommand(
                userAggregateId,
                responseUrl
        ));
        return responseUrl;
    }

    public String uploadProfileImage(MultipartFile profileImg) {
        String responseUrl;
        try {
            responseUrl = s3FileService.upload(profileImg, "profileImg");
        } catch (IOException e) {
            throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
        }
        return responseUrl;
    }
}
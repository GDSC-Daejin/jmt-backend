package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.UpdateUserNickNameCommand;
import com.gdsc.jmt.domain.user.command.UpdateUserProfileImgCommand;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import java.io.File;
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
    private final String PROFILE_IMAGE_URL = "/";

    @Transactional
    public void updateUserNickName(String userAggregateId, String nickName) {
        commandGateway.send(new UpdateUserNickNameCommand(
                userAggregateId,
                nickName
        ));
    }

    @Transactional
    public void updateUserProfileImg(String userAggregateId, MultipartFile profileImg) {
        uploadImage(profileImg);
        commandGateway.send(new UpdateUserProfileImgCommand(
                userAggregateId,
                PROFILE_IMAGE_URL + profileImg.getOriginalFilename()
        ));
    }

    private void uploadImage(MultipartFile image){
        try {
            File file = new File(PROFILE_IMAGE_URL);
            if (!file.exists()) { // 파일이 존재하지 않는다면 디렉토리 생성
                file.mkdirs();
            }
            File destination = new File(PROFILE_IMAGE_URL + File.separator + image.getOriginalFilename());
            image.transferTo(destination);
        } catch (IOException e) {
            throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
        }
    }
}

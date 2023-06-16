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
    private final String PROFILE_IMAGE_URL = "/src/main/resources/image/";

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
        try {
            responseUrl = s3FileService.upload(profileImg, "profileImg");
        } catch (IOException e) {
            throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
        }

        commandGateway.send(new UpdateUserProfileImgCommand(
                userAggregateId,
                responseUrl
        ));
        return responseUrl;
    }

    private void uploadImage(MultipartFile image){
        try {
            File file = new File(PROFILE_IMAGE_URL);
            Path path = Paths.get("");
            if (!file.exists()) { // 파일이 존재하지 않는다면 디렉토리 생성
                file.mkdirs();
            }
            File destination = new File(path.toAbsolutePath() + PROFILE_IMAGE_URL + File.separator + image.getOriginalFilename());
            image.transferTo(destination);
        } catch (IOException e) {
            throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
        }
    }
}

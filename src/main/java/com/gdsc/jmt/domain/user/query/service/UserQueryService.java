package com.gdsc.jmt.domain.user.query.service;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import com.gdsc.jmt.domain.user.query.dto.UserResponse;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public boolean checkDuplicateUserNickname(String nickname) {
        Optional<UserEntity> userEntity = userRepository.findByNickname(nickname);
        return userEntity.isPresent();
    }

    public UserResponse getUserInfo(String userAggregateId) {
        UserEntity user =  userRepository.findByUserAggregateId(userAggregateId)
                .orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));

        return new UserResponse(user.getEmail(), user.getNickname(), getProfileImg(user.getProfileImageUrl()));
    }

    private String getProfileImg(String profileImgUrl) {
        Path path = Paths.get("");
        return path.toAbsolutePath() + profileImgUrl;
    }
}

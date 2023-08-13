package com.gdsc.jmt.domain.user.query.service;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.user.query.dto.FindLocationListRequest;
import com.gdsc.jmt.domain.user.query.dto.KakaoLocationResponse;
import com.gdsc.jmt.domain.user.query.dto.UserResponse;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.domain.user.util.LocationAPIUtil;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    private final LocationAPIUtil locationAPIUtil;

    public boolean checkDuplicateUserNickname(String nickname) {
        Optional<UserEntity> userEntity = userRepository.findByNickname(nickname);
        return userEntity.isPresent();
    }

    public UserResponse getUserInfo(String email) {
        return new UserResponse(findByEmail(email));
    }
    private UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }

    public UserResponse findUser(Long userId) {
        return new UserResponse(findByUserId(userId));
    }

    private UserEntity findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }

    public List<KakaoSearchDocument> findLocation(final FindLocationListRequest findLocationListRequest) {
        KakaoLocationResponse kakaoLocationResponse = locationAPIUtil.findLocation(findLocationListRequest);
        return kakaoLocationResponse.getDocuments();
    }
}

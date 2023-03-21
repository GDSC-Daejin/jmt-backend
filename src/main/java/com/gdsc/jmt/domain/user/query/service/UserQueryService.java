package com.gdsc.jmt.domain.user.query.service;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public boolean checkDuplicateUserNickname(String nickname) {
        Optional<UserEntity> userEntity = userRepository.findByNickname(nickname);
        if(userEntity.isEmpty()) {
            return false;
        }
        return true;
    }
}

package com.gdsc.jmt.domain.user.manager;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import com.gdsc.jmt.domain.user.command.event.BaseUserEvent;
import com.gdsc.jmt.domain.user.command.event.CreateUserEvent;
import com.gdsc.jmt.domain.user.command.event.UpdateUserNickNameEvent;
import com.gdsc.jmt.domain.user.command.event.UpdateUserProfileImgEvent;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueryEntityManager {
    private final UserRepository userRepository;

    private final EventSourcingRepository<UserAggregate> userAggregateEventSourcingRepository;
    private final S3FileService s3FileService;

    @EventSourcingHandler
    public void on(CreateUserEvent event) {
        UserEntity userEntity = createQueryUser(getUserFromEvent(event));

        if(userEntity == null)
            return;

        persistUser(userEntity);
    }

    @EventSourcingHandler
    private void updateUserNickName(UpdateUserNickNameEvent updateUserNickNameEvent) {
        UserAggregate userAggregate = getUserFromEvent(updateUserNickNameEvent);
        checkExistingQueryUserByNickname(userAggregate.nickname);

        UserEntity updateUserEntity = findExistingQueryUserByAggregateId(userAggregate.id);
        updateUserEntity.setNickname(userAggregate.nickname);

        persistUser(updateUserEntity);
    }

    @EventSourcingHandler
    private void updateUserProfileImg(UpdateUserProfileImgEvent updateUserProfileImgEvent) {
        UserAggregate userAggregate = getUserFromEvent(updateUserProfileImgEvent);

        UserEntity updateUserEntity = findExistingQueryUserByAggregateId(userAggregate.id);
        if(updateUserEntity.getProfileImageUrl() != null) {
            s3FileService.delete(updateUserEntity.getProfileImageUrl());
        }
        updateUserEntity.setProfileImageUrl(userAggregate.profileImageUrl);

        persistUser(updateUserEntity);
    }

    private void checkExistingQueryUserByNickname(String nickname) {
        Optional<UserEntity> userEntity = userRepository.findByNickname(nickname);
        if(userEntity.isPresent()) {
            throw new ApiException(UserMessage.NICKNAME_IS_DUPLICATED);
        }
    }

    @EventSourcingHandler
    public void on(BaseUserEvent<String> event) {
        persistUser(updateQueryUser(getUserFromEvent(event)));
    }

    private UserAggregate getUserFromEvent(BaseUserEvent<String> event) {
        return userAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private UserEntity findExistingQueryUserByAggregateId(String id) {
        Optional<UserEntity> result = userRepository.findByUserAggregateId(id);
        return result.orElse(null);
    }

    private UserEntity findExistingOrCreateQueryUserByEmail(String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        return result.orElse(null);
    }

    private UserEntity createQueryUser(UserAggregate userAggregate) {
        if(findExistingOrCreateQueryUserByEmail(userAggregate.email) != null)
            return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserAggregateId(userAggregate.id);
        userEntity.setEmail(userAggregate.email);
        userEntity.setNickname(userAggregate.nickname);
        userEntity.setRoleType(userAggregate.roleType);
        userEntity.setProfileImageUrl(userAggregate.profileImageUrl);
        userEntity.setStatus(userAggregate.status);
        userEntity.setSocialType(userAggregate.socialType);

        return userEntity;
    }

    private UserEntity updateQueryUser(UserAggregate userAggregate) {
        UserEntity userEntity = findExistingQueryUserByAggregateId(userAggregate.id);

        userEntity.setEmail(userAggregate.email);
        userEntity.setNickname(userAggregate.nickname);
        userEntity.setProfileImageUrl(userAggregate.profileImageUrl);
        userEntity.setStatus(userAggregate.status);

        return userEntity;
    }

    private void persistUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}

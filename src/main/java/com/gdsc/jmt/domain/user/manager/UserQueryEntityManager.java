package com.gdsc.jmt.domain.user.manager;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import com.gdsc.jmt.domain.user.command.event.BaseUserEvent;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
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

    @EventSourcingHandler
    public void on(BaseUserEvent<String> event) {
        persistUser(buildQueryAccount(getUserFromEvent(event)));
    }

    private UserAggregate getUserFromEvent(BaseUserEvent<String> event) {
        return userAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private UserEntity findExistingOrCreateQueryUser(String id) {
        Optional<UserEntity> result = userRepository.findByUserAggregateId(id);
        return result.orElseGet(UserEntity::new);
    }

    private UserEntity buildQueryAccount(UserAggregate userAggregate) {
        UserEntity userEntity = findExistingOrCreateQueryUser(userAggregate.id);

        userEntity.setUserAggregateId(userAggregate.id);
        userEntity.setEmail(userAggregate.email);
        userEntity.setNickname(userAggregate.nickname);
        userEntity.setRoleType(userAggregate.roleType);
        userEntity.setProfileImageUrl(userAggregate.profileImageUrl);
        userEntity.setStatus(userAggregate.status);

        return userEntity;
    }

    private void persistUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}

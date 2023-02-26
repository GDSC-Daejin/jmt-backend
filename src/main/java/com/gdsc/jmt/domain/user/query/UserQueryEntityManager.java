package com.gdsc.jmt.domain.user.query;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueryEntityManager {
    private final UserRepository userRepository;

    private EventSourcingRepository<UserAggregate> userAggregateEventSourcingRepository;

    @EventSourcingHandler
    // TODO : 궁금점 첫째 : BaseEvent로 하면 어떤 이벤트가 발생할때마다 발생하게되는데 그러면 타 도메인 이벤트일때도 발생할텐데??
    // 해결방법 : BaseEvent가 아닌 BaseUserEvent 로 정의하면 되긴함.
    public void on(BaseEvent<String> event) {
        persistUser(buildQueryAccount(getUserFromEvent(event)));
    }

    private UserAggregate getUserFromEvent(BaseEvent<String> event) {
        return userAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private UserEntity findExistingOrCreateQueryAccount(String id) {
        Optional<UserEntity> result = userRepository.findByUserAggregateId(id);
        return result.orElseGet(UserEntity::new);
    }

    private UserEntity buildQueryAccount(UserAggregate userAggregate) {
        UserEntity userEntity = findExistingOrCreateQueryAccount(userAggregate.id);

        userEntity.userAggregateId = userAggregate.id;
        userEntity.email = userAggregate.email;
        userEntity.nickname = userAggregate.nickname;
        userEntity.roleType = userAggregate.roleType;
        userEntity.profileImageUrl = userAggregate.profileImageUrl;
        userEntity.status = userAggregate.status;

        return userEntity;
    }

    private void persistUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}

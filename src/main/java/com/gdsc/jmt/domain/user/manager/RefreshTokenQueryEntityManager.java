package com.gdsc.jmt.domain.user.manager;

import com.gdsc.jmt.domain.user.command.aggregate.RefreshTokenAggregate;
import com.gdsc.jmt.domain.user.command.event.BaseRefreshTokenEvent;
import com.gdsc.jmt.domain.user.query.entity.RefreshTokenEntity;
import com.gdsc.jmt.domain.user.query.repository.RefreshTokenRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenQueryEntityManager {
    private final RefreshTokenRepository refreshTokenRepository;

    private final EventSourcingRepository<RefreshTokenAggregate> refreshTokenAggregateEventSourcingRepository;

    @EventSourcingHandler
    public void on(BaseRefreshTokenEvent<String> event) {
        persistRefreshToken(buildQueryAccount(getRefreshTokenFromEvent(event)));
    }

    private RefreshTokenAggregate getRefreshTokenFromEvent(BaseRefreshTokenEvent<String> event) {
        return refreshTokenAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private void checkExisting(Long userId) {
        Optional<RefreshTokenEntity> result = refreshTokenRepository.findByUserId(userId);
        if(result.isPresent())
            throw new ApiException(UserMessage.LOGIN_CONFLICT);
    }

    private RefreshTokenEntity buildQueryAccount(RefreshTokenAggregate refreshTokenAggregate) {
        checkExisting(refreshTokenAggregate.userId);

        return new RefreshTokenEntity(
                refreshTokenAggregate.userId,
                refreshTokenAggregate.refreshToken
        );
    }

    private void persistRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.save(refreshTokenEntity);
    }
}

package com.gdsc.jmt.domain.user.manager;

import com.gdsc.jmt.domain.user.command.aggregate.RefreshTokenAggregate;
import com.gdsc.jmt.domain.user.command.event.BaseRefreshTokenEvent;
import com.gdsc.jmt.domain.user.command.event.LogoutEvent;
import com.gdsc.jmt.domain.user.query.entity.RefreshTokenEntity;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.RefreshTokenRepository;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EventSourcingRepository<RefreshTokenAggregate> refreshTokenAggregateEventSourcingRepository;

    @EventSourcingHandler
    public void login(BaseRefreshTokenEvent<String> event) {
        persistRefreshToken(buildQueryAccount(getRefreshTokenFromEvent(event)));
    }

    @EventSourcingHandler
    public void logout(LogoutEvent event) {
        UserEntity userEntity = checkExistingUserByEmail(event.getEmail());
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshToken(userEntity.getId());

        if(event.getRefreshToken().equals(refreshTokenEntity.getRefreshToken()))
            deleteRefreshToken(refreshTokenEntity);
        else
            throw new ApiException(UserMessage.LOGOUT_FAIL);
    }

    private RefreshTokenAggregate getRefreshTokenFromEvent(BaseRefreshTokenEvent<String> event) {
        return refreshTokenAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private UserEntity checkExistingUserByEmail(String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        if(result.isPresent())
            return result.get();
        else
            throw new ApiException(UserMessage.USER_NOT_FOUND);
    }

    private RefreshTokenEntity checkExistingRefreshToken(Long userId) {
        Optional<RefreshTokenEntity> result = refreshTokenRepository.findByUserId(userId);
        return result.orElse(null);
    }

    private RefreshTokenEntity buildQueryAccount(RefreshTokenAggregate refreshTokenAggregate) {
        UserEntity userEntity = checkExistingUserByEmail(refreshTokenAggregate.email);
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshToken(userEntity.getId());

        if(refreshTokenEntity == null) {
            return new RefreshTokenEntity(
                    userEntity.getId(),
                    refreshTokenAggregate.refreshToken
            );
        }
        else {
            refreshTokenEntity.setRefreshToken(refreshTokenAggregate.refreshToken);
            return refreshTokenEntity;
        }
    }

    private void persistRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void deleteRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.delete(refreshTokenEntity);
    }
}

package com.gdsc.jmt.domain.user.manager;

import com.gdsc.jmt.domain.user.command.aggregate.RefreshTokenAggregate;
import com.gdsc.jmt.domain.user.command.event.BaseRefreshTokenEvent;
import com.gdsc.jmt.domain.user.command.event.LogoutEvent;
import com.gdsc.jmt.domain.user.command.event.PersistRefreshTokenEvent;
import com.gdsc.jmt.domain.user.command.info.Reissue;
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
    public void login(PersistRefreshTokenEvent event) {
        if(event.getReissue() != null) {
            validateReissue(event.getEmail(), event.getReissue());
        }

        persistRefreshToken(buildQueryAccount(getRefreshTokenFromEvent(event), event.getEmail()));
    }

    @EventSourcingHandler
    public void logout(LogoutEvent event) {
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshTokenByEmail(event.getEmail());

        if(isValidateRefreshToken(event.getRefreshToken(), refreshTokenEntity.getRefreshToken()))
            deleteRefreshToken(refreshTokenEntity);
        else
            throw new ApiException(UserMessage.LOGOUT_FAIL);
    }

    private void validateReissue(String email , Reissue reissue) {
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshTokenByEmail(email);
        if(!isValidateRefreshToken(reissue.getOldRefreshToken(), refreshTokenEntity.getRefreshToken()))
            throw new ApiException(UserMessage.REISSUE_FAIL);
    }

    private RefreshTokenAggregate getRefreshTokenFromEvent(BaseRefreshTokenEvent<String> event) {
        return refreshTokenAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private RefreshTokenEntity checkExistingRefreshTokenByEmail(String email) {
        UserEntity userEntity = checkExistingUserByUserEmail(email);
        return checkExistingRefreshToken(userEntity.getId());
    }

    private UserEntity checkExistingUserByUserEmail(String email) {
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

    private RefreshTokenEntity buildQueryAccount(RefreshTokenAggregate refreshTokenAggregate, String email) {
        UserEntity userEntity = checkExistingUserByUserEmail(email);
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

    private boolean isValidateRefreshToken(String aggregateRefreshToken , String queryRefreshToken) {
        return aggregateRefreshToken.equals(queryRefreshToken);
    }

    private void persistRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void deleteRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.delete(refreshTokenEntity);
    }
}

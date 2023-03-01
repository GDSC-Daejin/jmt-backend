package com.gdsc.jmt.domain.user.command.aggregate;

import com.gdsc.jmt.domain.user.command.LogoutCommand;
import com.gdsc.jmt.domain.user.command.PersistRefreshTokenCommand;
import com.gdsc.jmt.domain.user.command.event.LogoutEvent;
import com.gdsc.jmt.domain.user.command.event.PersistRefreshTokenEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

// TODO : 도메인 관점에서 이것도 Aggregate 처리를 하는게 맞나? 의구심이 들음
// 근데 EventStore 사용하려면 맞는것 같기도 하고?
@Aggregate
@RequiredArgsConstructor
public class RefreshTokenAggregate {
    @AggregateIdentifier
    public String id;
    public String email;
    public String refreshToken;

    @CommandHandler
    public RefreshTokenAggregate(PersistRefreshTokenCommand persistRefreshTokenCommand) {
        AggregateLifecycle.apply(new PersistRefreshTokenEvent(
                persistRefreshTokenCommand.getId(),
                persistRefreshTokenCommand.getEmail(),
                persistRefreshTokenCommand.getRefreshToken()
        ));
    }

    @CommandHandler
    public void logout(LogoutCommand logoutCommand) {
        AggregateLifecycle.apply(new LogoutEvent(
                logoutCommand.getEmail(),
                logoutCommand.getRefreshToken()
        ));
    }

    @EventSourcingHandler
    public void on(PersistRefreshTokenEvent persistRefreshTokenEvent) {
        this.id = persistRefreshTokenEvent.getId();
        this.email = persistRefreshTokenEvent.getEmail();
        this.refreshToken = persistRefreshTokenEvent.getRefreshToken();
    }
}

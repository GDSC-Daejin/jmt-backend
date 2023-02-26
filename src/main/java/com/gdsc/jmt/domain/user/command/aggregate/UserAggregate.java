package com.gdsc.jmt.domain.user.command.aggregate;

import com.gdsc.jmt.domain.user.command.GoogleLoginCommand;
import com.gdsc.jmt.domain.user.command.event.CreateUserEvent;
import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.common.Status;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class UserAggregate {
    // TODO : 해당 id를 query DB에서 pk로 사용을 해야될지, 아니면 특정 컬럼으로 넣어야 할지 고민
    @AggregateIdentifier
    public String id;

    // TODO : 내용 자체는 QueryEntity랑 중복인데 abstract class 만들까???
    // 아 하면 할수록 코틀린 마렵네
    public String email;
    public String profileImageUrl;
    public String nickname;
    public RoleType roleType;
    public Status status;

    @EventSourcingHandler
    public void on(CreateUserEvent createUserEvent) {
        this.id = createUserEvent.getId();
        this.email = createUserEvent.getEmail();
        this.status = Status.ACTIVE;
    }

    @CommandHandler
    public void on(GoogleLoginCommand googleLoginCommand) {
        AggregateLifecycle.apply(new CreateUserEvent(
                googleLoginCommand.getId(),
                googleLoginCommand.getUserInfo().getEmail()
        ));
    }
}

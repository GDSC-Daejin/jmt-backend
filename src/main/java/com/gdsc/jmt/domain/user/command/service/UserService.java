package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.UpdateUserNickNameCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommandGateway commandGateway;

    @Transactional
    public void updateUserNickName(String userAggregateId, String nickName) {
        commandGateway.send(new UpdateUserNickNameCommand(
                userAggregateId,
                nickName
        ));
    }
}

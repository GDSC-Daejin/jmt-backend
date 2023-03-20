package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.command.UpdateUserNickNameCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommandGateway commandGateway;

    public void updateUserNickName(String AggregateId, String nickName) {
        commandGateway.send(new UpdateUserNickNameCommand(
                AggregateId,
                nickName
        ));
    }
}

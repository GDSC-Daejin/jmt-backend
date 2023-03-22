package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class UpdateUserNickNameEvent extends BaseUserEvent<String>{
    private final String nickName;
    public UpdateUserNickNameEvent(String id, String nickName) {
        super(id);
        this.nickName = nickName;
    }
}

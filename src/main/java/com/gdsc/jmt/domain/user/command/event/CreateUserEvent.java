package com.gdsc.jmt.domain.user.command.event;

import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateUserEvent extends BaseEvent<String> {
    private final String email;

    public CreateUserEvent(String id, String email) {
        super(id);
        this.email = email;
    }
}

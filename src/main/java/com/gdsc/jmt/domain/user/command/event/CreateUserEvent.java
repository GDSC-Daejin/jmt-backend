package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class CreateUserEvent extends BaseUserEvent<String> {
    private final String email;

    public CreateUserEvent(String id, String email) {
        super(id);
        this.email = email;
    }
}

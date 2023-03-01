package com.gdsc.jmt.domain.user.command;


import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class LogoutCommand extends BaseCommand<String> {
    private final String email;
    private final String refreshToken;

    public LogoutCommand(String id, String email, String refreshToken) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
    }
}

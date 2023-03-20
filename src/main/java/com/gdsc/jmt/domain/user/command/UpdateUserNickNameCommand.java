package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class UpdateUserNickNameCommand extends BaseCommand<String> {
    private final String nickName;

    public UpdateUserNickNameCommand(String id, String nickName) {
        super(id);
        this.nickName = nickName;
    }
}

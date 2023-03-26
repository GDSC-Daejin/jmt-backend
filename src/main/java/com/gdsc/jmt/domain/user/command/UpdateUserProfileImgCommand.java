package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class UpdateUserProfileImgCommand extends BaseCommand<String> {
    private final String profileImageUrl;

    public UpdateUserProfileImgCommand(String id, String profileImageUrl) {
        super(id);
        this.profileImageUrl = profileImageUrl;
    }
}

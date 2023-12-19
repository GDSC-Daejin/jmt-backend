package com.gdsc.jmt.domain.group.command.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateGroupRequest {
    public String groupName;

    public String groupIntroduce;

    public boolean isPrivateGroup;

    MultipartFile groupProfileImage;
    MultipartFile groupBackgroundImage;
}

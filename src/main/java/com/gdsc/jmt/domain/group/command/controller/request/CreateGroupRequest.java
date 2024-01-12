package com.gdsc.jmt.domain.group.command.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateGroupRequest {
    public String groupName;

    public String groupIntroduce;

    @JsonProperty("isPrivateGroup")
    public boolean isPrivateGroup;

    MultipartFile groupProfileImage;
    MultipartFile groupBackgroundImage;
}

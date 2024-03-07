package com.gdsc.jmt.domain.group.command.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindGroupResponse {
    private Long groupId;
    private String groupName;
    private String groupIntroduce;
    private boolean isPrivateGroup;
    private String groupProfileImageUrl;
    private String groupBackgroundImageUrl;
    private int memberCnt;
    private int restaurantCnt;

    @JsonProperty("isSelected")
    private boolean isSelected;
}

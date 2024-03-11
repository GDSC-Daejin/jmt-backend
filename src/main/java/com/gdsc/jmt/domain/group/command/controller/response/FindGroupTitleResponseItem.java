package com.gdsc.jmt.domain.group.command.controller.response;

import lombok.Builder;

@Builder
public record FindGroupTitleResponseItem(
        Long groupId,
        String groupName,
        String groupIntroduce,
        int memberCnt,
        int restaurantCnt
) {
}

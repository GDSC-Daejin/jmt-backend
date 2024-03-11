package com.gdsc.jmt.domain.group.command.controller.response;

import com.gdsc.jmt.domain.restaurant.query.dto.PageMeta;
import com.gdsc.jmt.global.dto.PageResponse;
import java.util.List;

public record FindGroupResponse(
        List<FindGroupTitleResponseItem> groupList,
        PageResponse page
) {
}

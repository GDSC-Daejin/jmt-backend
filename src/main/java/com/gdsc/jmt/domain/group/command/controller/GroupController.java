package com.gdsc.jmt.domain.group.command.controller;

import com.gdsc.jmt.domain.category.query.dto.CategoriesResponse;
import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.command.service.GroupService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.GroupMessage;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "그룹 컨트롤러")
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "그룹 생성 API", description = "그룹 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @PostMapping(value = "/group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JMTApiResponse<Null> createGroup(@ModelAttribute CreateGroupRequest createGroupRequest, @AuthenticationPrincipal UserInfo user) {
        groupService.createGroup(createGroupRequest, user);
        return JMTApiResponse.createResponseWithMessage(null, GroupMessage.CREATED_GROUP);
    }
}

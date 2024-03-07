package com.gdsc.jmt.domain.group.command.controller;

import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.command.controller.response.CreateGroupResponse;
import com.gdsc.jmt.domain.group.command.controller.response.FindGroupResponse;
import com.gdsc.jmt.domain.group.command.controller.response.LeaveGroupResponse;
import com.gdsc.jmt.domain.group.command.service.GroupService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.GroupMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public JMTApiResponse<CreateGroupResponse> createGroup(@ModelAttribute CreateGroupRequest createGroupRequest, @AuthenticationPrincipal UserInfo user) {
        Long groupCode = groupService.createGroup(createGroupRequest, user);
        return JMTApiResponse.createResponseWithMessage(new CreateGroupResponse(groupCode), GroupMessage.CREATED_GROUP);
    }

    @Operation(summary = "그룹 조회 API", description = "그룹 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @GetMapping(value = "/group/{groupId}")
    public JMTApiResponse<FindGroupResponse> findGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserInfo user) {
        FindGroupResponse findGroupResponse = groupService.findGroupById(groupId);
        return JMTApiResponse.createResponseWithMessage(findGroupResponse, GroupMessage.FIND_GROUP);
    }

    @Operation(summary = "사용자가 속한 유저 그룹 API", description = "사용자가 속한 유저 그룹 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @GetMapping(value = "/group/my")
    public JMTApiResponse<List<FindGroupResponse>> findUserGroupList(@AuthenticationPrincipal UserInfo user) {
        List<FindGroupResponse> findGroupsResponse = groupService.findUserGroupList(user);
        return JMTApiResponse.createResponseWithMessage(findGroupsResponse, GroupMessage.FIND_GROUP);
    }

    @Operation(summary = "그룹 가입 API", description = "가입 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @PostMapping(value = "/group/{groupId}/user")
    public JMTApiResponse<?> joinGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserInfo user) {
        groupService.joinGroup(groupId, user);
        return JMTApiResponse.createResponseWithMessage(null, GroupMessage.JOIN_GROUP);
    }

    @Operation(summary = "그룹 탈퇴 API", description = "그룹 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @DeleteMapping(value = "/group/{groupId}")
    public JMTApiResponse<LeaveGroupResponse> leaveGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserInfo user) {
        groupService.leaveGroup(groupId, user);
        return JMTApiResponse.createResponseWithMessage(new LeaveGroupResponse(groupId), GroupMessage.LEAVE_GROUP);
    }

    @Operation(summary = "그룹 선택 API", description = "그룹 선택 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @PostMapping(value = "/group/{groupId}/select")
    public JMTApiResponse<?> selectGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserInfo user) {
        groupService.userSelectGroup(groupId, user);
        return JMTApiResponse.createResponseWithMessage(null, GroupMessage.SELECTED_GROUP);
    }
}

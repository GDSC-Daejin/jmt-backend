package com.gdsc.jmt.domain.group.command.service;

import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.entity.GroupEntity;
import com.gdsc.jmt.domain.group.repository.GroupRepository;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional
    public void createGroup(CreateGroupRequest request, UserInfo userInfo) {
        GroupEntity groupEntity = GroupEntity.builder()
                .groupName(request.groupName)
                .groupIntroduce(request.groupIntroduce)
                .privateFlag(request.isPrivateGroup)
                .build();
        // TODO : 그룹장 세팅 (GROUP_USERS)
        // TODO : 사진 등록
        groupRepository.save(groupEntity);
    }

    //  TODO : 만들 예정
    private void createGroupImage() {

    }
}

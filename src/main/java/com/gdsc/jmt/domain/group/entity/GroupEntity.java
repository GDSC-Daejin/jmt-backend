package com.gdsc.jmt.domain.group.entity;

import com.gdsc.jmt.domain.group.command.controller.response.FindGroupResponseItem;
import com.gdsc.jmt.domain.group.command.controller.response.FindGroupTitleResponseItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity @Table(name = "tb_group")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long gid;

    @Column(name = "group_name")
    public String groupName;

    @Column(name = "group_introduce")
    public String groupIntroduce;

    @Column(name = "group_profile_image_url")
    public String groupProfileImageUrl;

    @Column(name = "group_background_image_url")
    public String groupBackgroundImageUrl;

    @Column(name = "private_flag")
    public boolean privateFlag;

    public FindGroupResponseItem toResponse(int memberCnt, int restaurantCnt, boolean isSelected) {
        return FindGroupResponseItem.builder()
                .groupId(this.getGid())
                .groupName(this.getGroupName())
                .groupIntroduce(this.getGroupIntroduce())
                .isPrivateGroup(this.isPrivateFlag())
                .groupBackgroundImageUrl(this.getGroupBackgroundImageUrl())
                .groupProfileImageUrl(this.getGroupProfileImageUrl())
                .memberCnt(memberCnt)
                .restaurantCnt(restaurantCnt)
                .isSelected(isSelected)
                .build();
    }

    public FindGroupTitleResponseItem toFindGroupTitleResponse(int memberCnt, int restaurantCnt) {
        return FindGroupTitleResponseItem.builder()
                .groupId(this.getGid())
                .groupName(this.getGroupName())
                .groupProfileImageUrl(this.getGroupProfileImageUrl())
                .groupIntroduce(this.getGroupIntroduce())
                .memberCnt(memberCnt)
                .restaurantCnt(restaurantCnt)
                .build();
    }
}

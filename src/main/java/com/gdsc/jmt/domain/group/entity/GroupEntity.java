package com.gdsc.jmt.domain.group.entity;

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
}

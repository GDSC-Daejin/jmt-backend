package com.gdsc.jmt.domain.user.query.entity;

import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.common.Status;
import com.gdsc.jmt.global.entity.BaseTimeEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // 컬럼이름 흐음
    public String userAggregateId;

    public String email;
    public String profileImageUrl;
    @Nullable
    public String nickname;
    @Enumerated(EnumType.STRING)
    public RoleType roleType;
    @Enumerated(EnumType.STRING)
    public Status status;
}

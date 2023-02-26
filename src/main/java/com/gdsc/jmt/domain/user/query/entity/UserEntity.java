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
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 컬럼이름 흐음
    private String userAggregateId;
    private String email;
    private String profileImageUrl;
    @Nullable
    private String nickname;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Enumerated(EnumType.STRING)
    private Status status;
}

package com.gdsc.jmt.domain.group.entity;


import com.gdsc.jmt.domain.group.code.GroupUserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "tb_group_users")
public class GroupUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guid")
    public Long guid;

    @Column(name = "gid")
    public Long groupId;

    @Column(name = "user_id")
    public Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    public GroupUserRole role;
}

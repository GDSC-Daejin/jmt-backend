package com.gdsc.jmt.domain.group.repository;

import com.gdsc.jmt.domain.group.entity.GroupUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUsersEntity, Long> {
    List<GroupUsersEntity> findByUserId(long userId);

    Optional<GroupUsersEntity> findByGroupIdAndUserId(long groupId, long userId);
}

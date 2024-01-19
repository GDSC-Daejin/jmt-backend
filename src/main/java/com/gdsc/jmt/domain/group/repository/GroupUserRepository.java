package com.gdsc.jmt.domain.group.repository;

import com.gdsc.jmt.domain.group.entity.GroupUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUsersEntity, Long> {
}

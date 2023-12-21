package com.gdsc.jmt.domain.group.repository;

import com.gdsc.jmt.domain.group.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
}

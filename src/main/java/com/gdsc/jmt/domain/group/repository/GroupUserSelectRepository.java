package com.gdsc.jmt.domain.group.repository;

import com.gdsc.jmt.domain.group.entity.GroupUserSelectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupUserSelectRepository extends JpaRepository<GroupUserSelectEntity, Long> {

    Optional<GroupUserSelectEntity> findByUserId(Long userId);
}

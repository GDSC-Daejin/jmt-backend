package com.gdsc.jmt.domain.group.repository;

import com.gdsc.jmt.domain.group.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    List<GroupEntity> findByGidIn(List<Long> gidList);

    @Query("select g from GroupEntity g where g.groupName like %:keyword%")
    Page<GroupEntity> findByGroupName(String keyword, Pageable pageable);
}

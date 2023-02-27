package com.gdsc.jmt.domain.user.query.repository;

import com.gdsc.jmt.domain.user.query.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}

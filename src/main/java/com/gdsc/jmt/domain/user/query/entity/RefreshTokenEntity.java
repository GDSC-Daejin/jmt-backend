package com.gdsc.jmt.domain.user.query.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="id", nullable = false)
    private UserEntity user;
    private Long userId;

    private String refreshToken;

    public RefreshTokenEntity(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}

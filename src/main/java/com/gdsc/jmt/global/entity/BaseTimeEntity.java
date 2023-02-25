package com.gdsc.jmt.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime createdTime;

    @Column(name = "modified_time", nullable = false, updatable = true, columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime modifiedTime;
}

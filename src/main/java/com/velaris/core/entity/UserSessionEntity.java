package com.velaris.core.entity;

import com.velaris.core.entity.common.BaseUuidEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_sessions")
public class UserSessionEntity extends BaseUuidEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @Column(length = 128)
    private String device;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 256)
    private String userAgent;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime lastUsedAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Column(length = 512)
    private String accessToken;

    @Column(length = 512)
    private String refreshToken;
}
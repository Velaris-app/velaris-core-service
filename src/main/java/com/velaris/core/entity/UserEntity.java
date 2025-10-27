package com.velaris.core.entity;

import com.velaris.core.entity.common.BaseUuidEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "users")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseUuidEntity {

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @Column(nullable = false)
    private String passwordHash;
}

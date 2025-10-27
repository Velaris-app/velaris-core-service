package com.velaris.core;

import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TestObjects {

    public static UserEntity user(UUID id, String username) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .email("user" + id + "@example.com")
                .passwordHash("hash")
                .build();
    }

    public static AssetEntity asset(UserEntity ownerId, String category, String name, BigDecimal price, int quantity) {
        return AssetEntity.builder()
                .name(name)
                .category(category)
                .purchasePrice(price)
                .quantity(quantity)
                .owner(ownerId)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}

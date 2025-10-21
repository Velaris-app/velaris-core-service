package com.velaris.core;

import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TestObjects {

    public static UserEntity user(Long id, String username) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .email("user" + id + "@example.com")
                .passwordHash("hash")
                .build();
    }

    public static AssetEntity asset(Long ownerId, String category, String name, BigDecimal price, int quantity) {
        return AssetEntity.builder()
                .name(name)
                .category(category)
                .purchasePrice(price)
                .quantity(quantity)
                .ownerId(ownerId)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}

package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface RecentActivityProjection {
    Long getId();
    UUID getOwnerId();
    Long getAssetId();
    String getName();
    String getCategory();
    BigDecimal getPurchasePrice();
    Integer getQuantity();
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
    String getActivityType();
}

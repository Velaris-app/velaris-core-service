package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface StatsTrendProjection {
    Long getId();
    UUID getOwnerId();
    OffsetDateTime getDate();
    BigDecimal getTotalValue();
    Integer getItemsAdded();
}

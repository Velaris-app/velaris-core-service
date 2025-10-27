package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface StatsCategoryTrendProjection {
    Long getId();
    UUID getOwnerId();
    String getCategory();
    OffsetDateTime getCreatedDate();
    BigDecimal getTotalValue();
}

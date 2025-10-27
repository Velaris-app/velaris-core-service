package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface StatsOverviewProjection {
    Long getId();
    UUID getOwnerId();
    BigDecimal getTotalValue();
    Integer getTotalItems();
    Long getAssetCount();
    String getCurrency();
}

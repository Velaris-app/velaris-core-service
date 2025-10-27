package com.velaris.core.entity.projection;

import java.math.BigDecimal;

public interface StatsTopHoldingsProjection {
    String getName();
    String getCategory();
    BigDecimal getTotalValue();
}

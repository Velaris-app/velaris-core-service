package com.velaris.core.entity.projection;

import java.math.BigDecimal;

public interface StatsTagProjection {
    String getTagName();
    Integer getAssetsCount();
    BigDecimal getTotalValue();
}

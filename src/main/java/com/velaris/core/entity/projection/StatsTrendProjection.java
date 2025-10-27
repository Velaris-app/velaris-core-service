package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface StatsTrendProjection {
    OffsetDateTime getDate();
    BigDecimal getTotalValue();
    Integer getItemsAdded();
}

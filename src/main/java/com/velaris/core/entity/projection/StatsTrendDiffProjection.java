package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface StatsTrendDiffProjection {
    Long getId();
    UUID getOwnerId();
    LocalDate getDate();
    BigDecimal getTotalValue();
    BigDecimal getDeltaValue();
    Double getDeltaPercent();
}

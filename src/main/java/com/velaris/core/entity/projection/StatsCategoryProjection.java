package com.velaris.core.entity.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface StatsCategoryProjection {
    Long getId();
    UUID getOwnerId();
    String getCategory();
    BigDecimal getTotalValue();
    Integer getItemCount();
    Long getUniqueAssets();
    Double getPercentageOfTotal();
}

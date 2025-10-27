package com.velaris.core.entity.projection;

import java.math.BigDecimal;

public interface StatsCategoryProjection {
    String getCategory();
    BigDecimal getTotalValue();
    Integer getItemCount();
    Long getUniqueAssets();
    Double getPercentageOfTotal();
}

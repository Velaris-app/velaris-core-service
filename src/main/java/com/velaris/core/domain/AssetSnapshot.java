package com.velaris.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AssetSnapshot {
    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal purchasePrice;
    private String currency;
    private String condition;
    private Integer purchaseYear;
    private Integer quantity;
    private Set<String> tags;
    private List<String> images;
    private UUID ownerId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

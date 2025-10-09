package com.velaris.core.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetEntity extends AuditableEntity {

    private String name;
    private String category;
    private String description;
    private BigDecimal purchasePrice;
    private String currency;
    private String condition;
    private Integer purchaseYear;
    private Integer quantity;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private List<String> tags;

    private Long ownerId;
}

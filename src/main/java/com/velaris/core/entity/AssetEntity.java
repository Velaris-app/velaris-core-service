package com.velaris.core.entity;

import com.querydsl.core.annotations.QueryEntity;
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
@QueryEntity
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
    @CollectionTable(
            name = "asset_images",
            joinColumns = @JoinColumn(name = "asset_id")
    )
    @Column(name = "image")
    private List<String> images;

    @ElementCollection
    @CollectionTable(
            name = "asset_tags",
            joinColumns = @JoinColumn(name = "asset_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    private Long ownerId;
}

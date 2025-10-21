package com.velaris.core.entity.view;

import com.velaris.core.entity.BaseView;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "stats_category_view")
public class StatsCategoryView extends BaseView {

    private Long ownerId;
    private String category;
    private BigDecimal totalValue;
    private Long itemCount;
    private Long uniqueAssets;
    private Double percentageOfTotal;
}
package com.velaris.core.entity.view;

import com.velaris.core.entity.BaseView;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "stats_category_trend_view")
public class StatsCategoryTrendView extends BaseView {

    private Long ownerId;
    private String category;
    private OffsetDateTime createdDate;
    private BigDecimal totalValue;
}

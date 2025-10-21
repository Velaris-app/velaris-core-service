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
@Table(name = "stats_trend_diff_view")
public class StatsTrendDiffView extends BaseView {

    private Long ownerId;
    private OffsetDateTime date;
    private BigDecimal totalValue;
    private BigDecimal deltaValue;
    private Double deltaPercent;
}

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
@Table(name = "stats_overview_view")
public class StatsOverviewView extends BaseView {

    private Long ownerId;
    private BigDecimal totalValue;
    private Long totalItems;
    private String currency;
    private Long assetCount;
}
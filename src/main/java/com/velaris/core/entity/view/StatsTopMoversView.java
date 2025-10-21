package com.velaris.core.entity.view;

import com.velaris.core.entity.BaseView;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "stats_top_movers_view")
public class StatsTopMoversView extends BaseView {

    private Long assetId;
    private Long ownerId;
    private String name;
    private String category;
    private BigDecimal totalValue;
    private BigDecimal deltaValue;
}

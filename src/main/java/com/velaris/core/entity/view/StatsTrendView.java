package com.velaris.core.entity.view;

import com.velaris.core.entity.BaseView;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "stats_trend_view")
public class StatsTrendView extends BaseView {

    private Long ownerId;
    private OffsetDateTime date;
    private BigDecimal totalValue;
    private Long itemsAdded;
}
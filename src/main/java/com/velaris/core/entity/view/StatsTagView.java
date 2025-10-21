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
@Table(name = "stats_tag_view")
public class StatsTagView extends BaseView {
    private Long ownerId;
    private String tag;
    private Long assetsCount;
    private BigDecimal totalValue;
}

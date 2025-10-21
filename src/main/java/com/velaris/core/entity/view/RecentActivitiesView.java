package com.velaris.core.entity.view;

import com.velaris.core.entity.BaseView;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "recent_activities_view")
public class RecentActivitiesView extends BaseView {
    private Long ownerId;
    private Long assetId;
    private String name;
    private String category;
    private BigDecimal purchasePrice;
    private Integer quantity;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
}

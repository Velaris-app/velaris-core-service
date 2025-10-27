package com.velaris.core.entity;

import com.velaris.core.entity.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "recent_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentActivityEntity extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private AssetEntity asset;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Lob
    private String snapshotBefore;

    @Lob
    private String snapshotAfter;
}

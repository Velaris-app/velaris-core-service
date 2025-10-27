package com.velaris.core.repository.jpa;

import com.velaris.core.entity.RecentActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRecentActivitiesRepository extends JpaRepository<RecentActivityEntity, Long> {

    List<RecentActivityEntity> findAllByAssetOwnerIdOrderByCreatedAtDesc(UUID ownerId);
    Optional<RecentActivityEntity> findByAssetId(UUID assetId);
}

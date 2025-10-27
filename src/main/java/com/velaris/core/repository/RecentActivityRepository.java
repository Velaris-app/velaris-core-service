package com.velaris.core.repository;

import com.velaris.core.entity.RecentActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecentActivityRepository extends JpaRepository<RecentActivityEntity, Long> {
    List<RecentActivityEntity> findAllByAssetOwnerIdOrderByCreatedAtDesc(UUID ownerId);
}

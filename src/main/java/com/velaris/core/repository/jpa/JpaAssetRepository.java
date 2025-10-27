package com.velaris.core.repository.jpa;

import com.velaris.core.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAssetRepository extends JpaRepository<AssetEntity, Long> {
    List<AssetEntity> findAllByOwnerId(UUID ownerId);
}

package com.velaris.core.repository;

import com.velaris.core.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    List<AssetEntity> findAllByOwnerId(Long userId);
}

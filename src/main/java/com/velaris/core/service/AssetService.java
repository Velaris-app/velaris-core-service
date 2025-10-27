package com.velaris.core.service;

import com.velaris.api.model.Asset;
import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.entity.ActivityType;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.repository.AssetRepository;
import com.velaris.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final RecentActivityService recentActivityService;
    private final UserRepository userRepository;

    @Transactional
    public Asset createAsset(UUID ownerId, Asset assetDto) {
        UserEntity owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AssetEntity asset = assetMapper.toEntity(assetDto);
        asset.setOwner(owner);

        var saved = assetRepository.save(asset);
        recentActivityService.logActivity(null, ActivityType.CREATED, asset);
        return assetMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public Asset getAssetById(String id) {
        return assetRepository.findById(Long.valueOf(id))
                .map(assetMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::toDto)
                .toList();
    }

    @Transactional
    public Asset updateAsset(String id, Asset assetDto) {
        AssetEntity existing = assetRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        assetMapper.updateEntity(assetDto, existing);
        AssetEntity updated = assetRepository.save(existing);
        recentActivityService.logActivity(updated, ActivityType.UPDATED, existing);
        return assetMapper.toDto(existing);
    }

    @Transactional
    public void deleteAsset(String id) {
        AssetEntity existing = assetRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        recentActivityService.logActivity(existing, ActivityType.DELETED, null);
        assetRepository.deleteById(existing.getId());
    }

    @Transactional(readOnly = true)
    public List<RecentActivitiesItem> getRecentActivitiesForUser(UUID userId) {
        return recentActivityService.getRecentActivitiesForUser(userId);
    }
}
package com.velaris.core.service;

import com.velaris.api.model.Asset;
import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.mapper.RecentActivitiesMapper;
import com.velaris.core.repository.AssetRepository;
import com.velaris.core.repository.view.RecentActivitiesViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final RecentActivitiesViewRepository activitiesRepository;
    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final RecentActivitiesMapper recentActivitiesMapper;

    @Transactional
    public Asset createAsset(Long ownerId, Asset assetDto) {
        AssetEntity entity = assetMapper.toEntity(assetDto);
        entity.setOwnerId(ownerId);
        return assetMapper.toDto(assetRepository.save(entity));
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
        assetRepository.save(existing);
        return assetMapper.toDto(existing);
    }

    @Transactional
    public void deleteAsset(String id) {
        assetRepository.deleteById(Long.valueOf(id));
    }

    @Transactional(readOnly = true)
    public List<RecentActivitiesItem> getRecentActivitiesForUser(Long userId) {
        return activitiesRepository.findAllByOwnerId(userId).stream()
                .map(recentActivitiesMapper::toDto)
                .toList();
    }
}
package com.velaris.core.service;

import com.velaris.api.model.Asset;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

    @Transactional
    public Asset createAsset(Asset assetDto) {
        AssetEntity entity = assetMapper.toEntity(assetDto);
        assetRepository.save(entity);
        return assetMapper.toDto(entity);
    }

    public Asset getAssetById(String id) {
        return assetRepository.findById(Long.valueOf(id))
                .map(assetMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

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
}
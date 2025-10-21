package com.velaris.core.controller;

import com.velaris.api.AssetsApi;
import com.velaris.api.model.Asset;
import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.velaris.shared.security.SecurityUtils.currentUserId;

@RestController
@RequiredArgsConstructor
public class AssetsController implements AssetsApi {

    private final AssetService assetService;

    @Override
    public ResponseEntity<Asset> addAsset(@Valid Asset asset) {
        return ResponseEntity.ok(assetService.createAsset(currentUserId(), asset));
    }

    @Override
    public ResponseEntity<Void> deleteAsset(String id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Asset> getAsset(String id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @Override
    public ResponseEntity<List<RecentActivitiesItem>> listAssetActivities() {
        return ResponseEntity.ok(assetService.getRecentActivitiesForUser(currentUserId()));
    }

    @Override
    public ResponseEntity<List<Asset>> listAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @Override
    public ResponseEntity<Asset> modifyAsset(String id, @Valid Asset asset) {
        return ResponseEntity.ok(assetService.updateAsset(id, asset));
    }
}
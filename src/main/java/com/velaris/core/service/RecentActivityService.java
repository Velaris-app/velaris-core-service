package com.velaris.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.entity.ActivityType;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.RecentActivityEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.mapper.RecentActivitiesMapper;
import com.velaris.core.repository.RecentActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentActivityService {

    private final RecentActivityRepository activitiesRepository;
    private final RecentActivitiesMapper mapper;
    private final AssetMapper assetMapper;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActivity(AssetEntity before, ActivityType type, AssetEntity after) {
        RecentActivityEntity activity = RecentActivityEntity.builder()
                .asset(before)
                .activityType(type)
                .snapshotAfter(objectMapper.writeValueAsString(assetMapper.toSnapshot(after)))
                .snapshotBefore(objectMapper.writeValueAsString(assetMapper.toSnapshot(before)))
                .build();
        activitiesRepository.save(activity);
    }

    public List<RecentActivitiesItem> getRecentActivitiesForUser(UUID ownerId) {
        return activitiesRepository.findAllByAssetOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(e -> {
                    RecentActivitiesItem dto = mapper.toDto(e);
                    dto.changedFields(Map.of());
                    if (e.getActivityType() == ActivityType.UPDATED) {
                        dto.changedFields(computeChangedFields(e));
                    }
                    return dto;
                })
                .toList();

    }

    @SneakyThrows
    private Map<String, String> computeChangedFields(RecentActivityEntity e) {
        var before = objectMapper.readValue(e.getSnapshotBefore(), new TypeReference<Map<String, Object>>() {});
        var after = objectMapper.readValue(e.getSnapshotAfter(), new TypeReference<Map<String, Object>>() {});

        return after.entrySet().stream()
                .filter(entry -> !Objects.equals(entry.getValue(), before.get(entry.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() != null ? entry.getValue().toString() : ""
                ));
    }
}
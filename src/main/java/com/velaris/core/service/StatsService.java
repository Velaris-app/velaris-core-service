package com.velaris.core.service;

import com.velaris.api.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.velaris.core.mapper.StatsMapper;
import com.velaris.core.repository.view.*;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsTagViewRepository statsTagRepo;
    private final StatsCategoryViewRepository categoryRepo;
    private final StatsTrendViewRepository trendRepo;
    private final StatsOverviewViewRepository overviewRepo;
    private final StatsTrendDiffViewRepository trendDiffRepo;
    private final StatsTopMoversViewRepository topMoversRepo;
    private final StatsCategoryTrendViewRepository categoryTrendRepo;
    private final StatsMapper mapper;

    public List<StatsCategoryItem> getCategoryStats(Long ownerId) {
        return categoryRepo.findAllByOwnerId(ownerId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<StatsTrendItem> getTrendStats(Long ownerId) {
        return trendRepo.findAllByOwnerId(ownerId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public StatsOverview getOverview(Long ownerId) {
        return mapper.toDto(overviewRepo.findByOwnerId(ownerId));
    }

    public List<StatsTagItem> getTagsStats(Long ownerId) {
        return statsTagRepo.findByOwnerId(ownerId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<StatsTrendDiffItem> getTrendDiffStats(Long ownerId) {
        return trendDiffRepo.findByOwnerIdOrderByDateAsc(ownerId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<StatsTopMoversItem> getTopMovers(Long ownerId) {
        return topMoversRepo.findByOwnerIdOrderByDeltaValueDesc(ownerId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<StatsCategoryTrendItem> getCategoryTrend(Long ownerId, String category) {
        return categoryTrendRepo.findByOwnerIdAndCategoryOrderByCreatedDateAsc(ownerId, category).stream()
                .map(mapper::toDto)
                .toList();
    }
}
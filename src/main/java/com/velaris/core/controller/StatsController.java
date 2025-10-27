package com.velaris.core.controller;

import com.velaris.api.StatsApi;
import com.velaris.api.model.*;
import com.velaris.core.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.velaris.shared.security.SecurityUtils.currentUserId;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;

    @Override
    public ResponseEntity<List<CategoryItem>> getStatsByCategory() {
        return ResponseEntity.ok(statsService.getCategoryStats(currentUserId()));
    }

    @Override
    public ResponseEntity<OverviewItem> getStatsOverview() {
        return ResponseEntity.ok(statsService.getOverview(currentUserId()));
    }

    @Override
    public ResponseEntity<List<TagItem>> getStatsTag() {
        return ResponseEntity.ok(statsService.getTagsStats(currentUserId()));
    }

    @Override
    public ResponseEntity<List<TrendItem>> getStatsTrend(@Valid TrendRequest trendRequest) {
        return ResponseEntity.ok(statsService.getTrendStats(currentUserId(), trendRequest));
    }

    @Override
    public ResponseEntity<List<TopHoldingItem>> getTopHoldings(@Valid SearchFilter searchFilter) {
        return ResponseEntity.ok(statsService.getTopHoldings(currentUserId(), searchFilter));
    }

    @Override
    public ResponseEntity<List<TrendDiffItem>> getTrendDiffStats() {
        return ResponseEntity.ok(statsService.getTrendDiffStats(currentUserId()));
    }

    @Override
    public ResponseEntity<List<CategoryTrendItem>> getCategoryTrend(CategoryTrendRequest request) {
        return ResponseEntity.ok(statsService.getCategoryTrend(currentUserId(), request));
    }
}

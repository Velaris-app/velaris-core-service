package com.velaris.core.controller;

import com.velaris.api.StatsApi;
import com.velaris.api.model.*;
import com.velaris.core.service.StatsService;
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
    public ResponseEntity<List<StatsCategoryItem>> getStatsByCategory() {
        return ResponseEntity.ok(statsService.getCategoryStats(currentUserId()));
    }

    @Override
    public ResponseEntity<StatsOverview> getStatsOverview() {
        return ResponseEntity.ok(statsService.getOverview(currentUserId()));
    }

    @Override
    public ResponseEntity<List<StatsTagItem>> getStatsTag() {
        return ResponseEntity.ok(statsService.getTagsStats(currentUserId()));
    }

    @Override
    public ResponseEntity<List<StatsTrendItem>> getStatsTrend() {
        return ResponseEntity.ok(statsService.getTrendStats(currentUserId()));
    }

    @Override
    public ResponseEntity<List<StatsTrendDiffItem>> getTrendDiffStats() {
        return ResponseEntity.ok(statsService.getTrendDiffStats(currentUserId()));
    }

    @Override
    public ResponseEntity<List<StatsTopMoversItem>> getTopMovers() {
        return ResponseEntity.ok(statsService.getTopMovers(currentUserId()));
    }

    @Override
    public ResponseEntity<List<StatsCategoryTrendItem>> getCategoryTrend(String category) {
        return ResponseEntity.ok(statsService.getCategoryTrend(currentUserId(), category));
    }
}

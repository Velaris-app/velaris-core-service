package com.velaris.core.controller;

import com.velaris.api.StatsApi;
import com.velaris.api.model.StatsCategoryItem;
import com.velaris.api.model.StatsOverview;
import com.velaris.api.model.StatsTrendItem;
import com.velaris.core.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;

    @Override
    public ResponseEntity<List<StatsCategoryItem>> getStatsByCategory() {
        return ResponseEntity.ok(statsService.getByCategory());
    }

    @Override
    public ResponseEntity<StatsOverview> getStatsOverview() {
        return ResponseEntity.ok(statsService.getOverview());
    }

    @Override
    public ResponseEntity<List<StatsTrendItem>> getStatsTrend() {
        return ResponseEntity.ok(statsService.getTrend());
    }
}

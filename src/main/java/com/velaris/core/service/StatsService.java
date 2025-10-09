package com.velaris.core.service;

import com.velaris.api.model.StatsCategoryItem;
import com.velaris.api.model.StatsOverview;
import com.velaris.api.model.StatsTrendItem;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.repository.AssetRepository;
import com.velaris.core.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final AssetRepository assetRepository;

    public StatsOverview getOverview() {
        Long userId = SecurityUtils.currentUserId();
        var assets = assetRepository.findAllByOwnerId(userId);

        BigDecimal total = assets.stream()
                .map(a -> safePrice(a).multiply(BigDecimal.valueOf(safeQuantity(a))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = assets.stream()
                .mapToInt(this::safeQuantity)
                .sum();

        return new StatsOverview()
                .totalValue(total)
                .currency("USD")
                .totalItems(count);
    }

    public List<StatsCategoryItem> getByCategory() {
        Long userId = SecurityUtils.currentUserId();
        var assets = assetRepository.findAllByOwnerId(userId);

        return assets.stream()
                .filter(a -> a.getCategory() != null)
                .collect(Collectors.groupingBy(AssetEntity::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    List<AssetEntity> assetList = entry.getValue();

                    BigDecimal totalValue = assetList.stream()
                            .map(a -> safePrice(a).multiply(BigDecimal.valueOf(safeQuantity(a))))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int itemCount = assetList.stream()
                            .mapToInt(this::safeQuantity)
                            .sum();

                    return new StatsCategoryItem()
                            .category(category)
                            .totalValue(totalValue)
                            .itemCount(itemCount);
                })
                .sorted(Comparator.comparing(StatsCategoryItem::getTotalValue).reversed())
                .toList();
    }

    public List<StatsTrendItem> getTrend() {
        Long userId = SecurityUtils.currentUserId();
        var assets = assetRepository.findAllByOwnerId(userId);

        return assets.stream()
                .filter(a -> a.getCreatedAt() != null)
                .collect(Collectors.groupingBy(a -> a.getCreatedAt().toLocalDate()))
                .entrySet().stream()
                .map(e -> new StatsTrendItem()
                        .date(e.getKey())
                        .value(e.getValue().stream()
                                .map(a -> safePrice(a).multiply(BigDecimal.valueOf(safeQuantity(a))))
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                )
                .sorted(Comparator.comparing(StatsTrendItem::getDate))
                .toList();
    }

    private BigDecimal safePrice(AssetEntity a) {
        return a.getPurchasePrice() != null ? a.getPurchasePrice() : BigDecimal.ZERO;
    }

    private int safeQuantity(AssetEntity a) {
        return a.getQuantity() != null ? a.getQuantity() : 0;
    }
}
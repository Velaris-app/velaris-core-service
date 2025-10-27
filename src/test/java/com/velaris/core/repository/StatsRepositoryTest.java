package com.velaris.core.repository;

import com.velaris.core.RepositoryTest;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.entity.projection.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    private UUID ownerId;

    private OffsetDateTime now;

    @BeforeEach
    void setUp() {
        statsRepository.deleteAll();
        now = OffsetDateTime.now().withNano(0);

        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("testuser@test.com")
                .passwordHash("password")
                .build();

        userRepository.save(user);

        ownerId = user.getId();

        // Category: Cards
        AssetEntity a1 = AssetEntity.builder()
                .owner(user)
                .name("Card A")
                .category("Cards")
                .purchasePrice(BigDecimal.valueOf(10))
                .quantity(2)
                .createdAt(now.minusDays(2))
                .tags(Set.of("Rare"))
                .build();

        AssetEntity a2 = AssetEntity.builder()
                .owner(user)
                .name("Card B")
                .category("Cards")
                .purchasePrice(BigDecimal.valueOf(20))
                .quantity(1)
                .createdAt(now.minusDays(1))
                .tags(Set.of("Common"))
                .build();

        AssetEntity a3 = AssetEntity.builder()
                .owner(user)
                .name("Lego Set")
                .category("Lego")
                .purchasePrice(BigDecimal.valueOf(50))
                .quantity(1)
                .createdAt(now)
                .tags(Set.of("Exclusive"))
                .build();

        assetRepository.saveAll(List.of(a1, a2, a3));
    }

    @Test
    void testGetCategoryStats() {
        List<StatsCategoryProjection> categories = statsRepository.getCategoryStats(ownerId);

        assertThat(categories).hasSize(2);
        assertThat(categories).extracting("category").containsExactlyInAnyOrder("Cards", "Lego");
    }

    @Test
    void testGetOverview() {
        StatsOverviewProjection overview = statsRepository.getOverview(ownerId);

        assertThat(overview.getTotalValue()).isEqualByComparingTo(BigDecimal.valueOf(90));
        assertThat(overview.getTotalItems()).isEqualTo(4);
        assertThat(overview.getAssetCount()).isEqualTo(3);
    }

    @Test
    void testGetTrendStats() {
        OffsetDateTime start = now.minusDays(3);
        OffsetDateTime end = now;

        List<StatsTrendProjection> trends = statsRepository.getTrendStats(ownerId, start, end);

        assertThat(trends).hasSize(3);
    }

    @Test
    void testGetTopHoldings() {
        List<StatsTopHoldingsProjection> topHoldings = statsRepository.getTopHoldings(ownerId, PageRequest.of(0, 2));

        assertThat(topHoldings).hasSize(2);
        assertThat(topHoldings).extracting("category").containsAnyOf("Cards", "Lego");
    }

    @Test
    void testGetTagsStats() {
        List<StatsTagProjection> tagStats = statsRepository.getTagsStats(ownerId);

        assertThat(tagStats).hasSize(3);
        assertThat(tagStats).extracting("tagName").containsExactlyInAnyOrder("Rare", "Common", "Exclusive");
    }

    @Test
    void testGetCategoryTrend() {
        OffsetDateTime start = now.minusDays(3);
        OffsetDateTime end = now;

        List<StatsCategoryTrendProjection> trend = statsRepository.getCategoryTrend(ownerId, "Cards", start, end);

        assertThat(trend)
                .isNotEmpty()
                .allMatch(t -> t.getCategory().equals("Cards"))
                .hasSize(2);
    }
}

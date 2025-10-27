package com.velaris.core.repository;

import com.velaris.core.RepositoryTest;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.entity.projection.*;
import com.velaris.core.repository.jpa.JpaAssetRepository;
import com.velaris.core.repository.jpa.JpaStatsRepository;
import com.velaris.core.repository.jpa.JpaUserRepository;
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
class JpaStatsRepositoryTest {

    @Autowired
    private JpaStatsRepository jpaStatsRepository;

    @Autowired
    private JpaAssetRepository jpaAssetRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private UUID ownerId;

    @BeforeEach
    void setUp() {
        jpaStatsRepository.deleteAll();

        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("testuser@test.com")
                .passwordHash("password")
                .build();

        jpaUserRepository.save(user);

        // Category: Cards
        AssetEntity a1 = AssetEntity.builder()
                .owner(user)
                .name("Card A")
                .category("Cards")
                .purchasePrice(BigDecimal.valueOf(10))
                .quantity(2)
                .createdAt(OffsetDateTime.now().minusDays(2))
                .tags(Set.of("Rare"))
                .build();

        // Category: Cards
        AssetEntity a2 = AssetEntity.builder()
                .owner(user)
                .name("Card B")
                .category("Cards")
                .purchasePrice(BigDecimal.valueOf(20))
                .quantity(1)
                .createdAt(OffsetDateTime.now().minusDays(1))
                .tags(Set.of("Common"))
                .build();

        // Category: Lego
        AssetEntity a3 = AssetEntity.builder()
                .owner(user)
                .name("Lego Set")
                .category("Lego")
                .purchasePrice(BigDecimal.valueOf(50))
                .quantity(1)
                .createdAt(OffsetDateTime.now())
                .tags(Set.of("Exclusive"))
                .build();

        jpaAssetRepository.saveAll(List.of(a1, a2, a3));
    }

    @Test
    void testGetCategoryStats() {
        List<StatsCategoryProjection> categories = jpaStatsRepository.getCategoryStats(ownerId);

        assertThat(categories).hasSize(2);
        assertThat(categories).extracting("category").containsExactlyInAnyOrder("Cards", "Lego");
    }

    @Test
    void testGetOverview() {
        StatsOverviewProjection overview = jpaStatsRepository.getOverview(ownerId);

        assertThat(overview.getTotalValue()).isEqualByComparingTo(BigDecimal.valueOf(90));
        assertThat(overview.getTotalItems()).isEqualTo(4);
        assertThat(overview.getAssetCount()).isEqualTo(3);
    }

    @Test
    void testGetTrendStats() {
        OffsetDateTime start = OffsetDateTime.now().minusDays(3);
        OffsetDateTime end = OffsetDateTime.now();

        List<StatsTrendProjection> trends = jpaStatsRepository.getTrendStats(ownerId, start, end);

        assertThat(trends).hasSize(3);
    }

    @Test
    void testGetTopHoldings() {
        List<StatsTopHoldingsProjection> topHoldings = jpaStatsRepository.getTopHoldings(ownerId, PageRequest.of(0, 2));

        assertThat(topHoldings).hasSize(2);
        assertThat(topHoldings).extracting("category").containsAnyOf("Cards", "Lego");
    }

    @Test
    void testGetTagsStats() {
        List<StatsTagProjection> tagStats = jpaStatsRepository.getTagsStats(ownerId);

        assertThat(tagStats).hasSize(3);
        assertThat(tagStats).extracting("tagName").containsExactlyInAnyOrder("Rare", "Common", "Exclusive");
    }

    @Test
    void testGetCategoryTrend() {
        OffsetDateTime start = OffsetDateTime.now().minusDays(3);
        OffsetDateTime end = OffsetDateTime.now();

        List<StatsCategoryTrendProjection> trend = jpaStatsRepository.getCategoryTrend(ownerId, "Cards", start, end);

        assertThat(trend)
                .isNotEmpty()
                .allMatch(t -> t.getCategory().equals("Cards"));
    }
}

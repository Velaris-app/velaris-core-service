package com.velaris.core.repository.jpa;

import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaStatsRepository extends JpaRepository<AssetEntity, Long> {

    @Query("""
        SELECT a.category AS category,
               COALESCE(SUM(a.purchasePrice * a.quantity), 0) AS totalValue,
               COALESCE(SUM(a.quantity), 0) AS totalItems,
               COUNT(a.id) AS assetCount
        FROM AssetEntity a
        WHERE a.owner.id = :ownerId AND a.category IS NOT NULL
        GROUP BY a.category
    """)
    List<StatsCategoryProjection> getCategoryStats(@Param("ownerId") UUID ownerId); // DONE

    @Query("""
        SELECT COALESCE(SUM(a.purchasePrice * a.quantity), 0) AS totalValue,
               'USD' AS currency,
               COALESCE(SUM(a.quantity), 0) AS totalItems,
               COUNT(a.id) AS assetCount
        FROM AssetEntity a
        WHERE a.owner.id = :ownerId
    """)
    StatsOverviewProjection getOverview(@Param("ownerId") UUID ownerId); // DONE

    @Query("""
        SELECT a.createdAt AS date,
               COALESCE(SUM(a.purchasePrice * a.quantity), 0) AS totalValue,
               COUNT(a.id) AS count
        FROM AssetEntity a
        WHERE a.owner.id = :ownerId
          AND (:startDate IS NULL OR a.createdAt >= :startDate)
          AND (:endDate IS NULL OR a.createdAt <= :endDate)
        GROUP BY a.createdAt
        ORDER BY a.createdAt ASC
    """)
    List<StatsTrendProjection> getTrendStats( // DONE
            @Param("ownerId") UUID ownerId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT a.name AS name,
               a.category AS category,
               (a.purchasePrice * a.quantity) AS totalValue
        FROM AssetEntity a
        WHERE a.owner.id = :ownerId
    """)
    List<StatsTopHoldingsProjection> getTopHoldings(
            @Param("ownerId") UUID ownerId,
            Pageable pageable); // DONE

    @Query("""
        SELECT t AS tagName,
               COUNT(a.id) AS assetsCount,
               COALESCE(SUM(a.purchasePrice * a.quantity), 0) AS totalValue
        FROM AssetEntity a
        JOIN a.tags t
        WHERE a.owner.id = :ownerId
        GROUP BY t
    """)
    List<StatsTagProjection> getTagsStats(@Param("ownerId") UUID ownerId);

    @Query("""
        SELECT a.category AS category,
               a.createdAt AS createdDate,
               COALESCE(SUM(a.purchasePrice * a.quantity), 0) AS totalValue
        FROM AssetEntity a
        WHERE a.owner.id = :ownerId
          AND (:category IS NULL OR a.category = :category)
          AND (:startDate IS NULL OR a.createdAt >= :startDate)
          AND (:endDate IS NULL OR a.createdAt <= :endDate)
        GROUP BY a.category, a.createdAt
        ORDER BY a.createdAt
    """)
    List<StatsCategoryTrendProjection> getCategoryTrend( // DONE
            @Param("ownerId") UUID ownerId,
            @Param("category") String category,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);
}
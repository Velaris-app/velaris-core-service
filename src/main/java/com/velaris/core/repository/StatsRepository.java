package com.velaris.core.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.velaris.api.model.*;
import com.velaris.core.entity.QAssetEntity;
import com.velaris.core.mapper.StatsMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatsRepository {

    private final JPAQueryFactory queryFactory;
    private final StatsMapper mapper;

    // ===================== CATEGORY STATS =====================
    public List<StatsCategoryItem> getCategoryStats(Long ownerId) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        return queryFactory
                .select(Projections.constructor(
                        StatsCategoryItem.class,
                        asset.category,
                        asset.purchasePrice.multiply(asset.quantity).sum().coalesce(BigDecimal.ZERO),
                        asset.quantity.sum().coalesce(0),
                        asset.id.countDistinct()
                ))
                .from(asset)
                .where(asset.ownerId.eq(ownerId).and(asset.category.isNotNull()))
                .groupBy(asset.category)
                .fetch();
    }

    // ===================== OVERVIEW =====================
    public StatsOverview getOverview(Long ownerId) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        return queryFactory
                .select(Projections.bean(
                        StatsOverview.class,
                        asset.purchasePrice.multiply(asset.quantity).sum().coalesce(BigDecimal.ZERO).as("totalValue"),
                        ExpressionUtils.as(Expressions.constant("USD"), "currency"),
                        asset.quantity.sum().coalesce(0).as("totalItems"),
                        asset.id.count().as("assetCount")
                ))
                .from(asset)
                .where(asset.ownerId.eq(ownerId))
                .fetchOne();
    }

    // ===================== TREND =====================
    public List<StatsTrendItem> getTrendStats(Long ownerId) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        return queryFactory
                .select(Projections.constructor(
                        StatsTrendItem.class,
                        asset.createdAt.as("date"),
                        asset.purchasePrice.multiply(asset.quantity).sum().coalesce(BigDecimal.ZERO),
                        asset.id.count()
                ))
                .from(asset)
                .where(asset.ownerId.eq(ownerId))
                .groupBy(asset.createdAt)
                .orderBy(asset.createdAt.asc())
                .fetch();
    }

    // ===================== TOP MOVERS =====================
    public List<StatsTopMoversItem> getTopMovers(Long ownerId) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        return queryFactory
                .select(Projections.constructor(
                        StatsTopMoversItem.class,
                        asset.id,
                        asset.name,
                        asset.category,
                        asset.purchasePrice.multiply(asset.quantity),
                        asset.purchasePrice.multiply(asset.quantity) // można liczyć delta w innym miejscu
                ))
                .from(asset)
                .where(asset.ownerId.eq(ownerId))
                .orderBy(asset.purchasePrice.multiply(asset.quantity).desc())
                .fetch();
    }

    // ===================== TAG STATS =====================
    public List<StatsTagItem> getTagsStats(Long ownerId) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        // zakładam, że masz encję AssetTag z assetId i tag
        // tutaj pokazuję przykładowo prostą agregację
        return queryFactory
                .select(Projections.constructor(
                        StatsTagItem.class,
                        asset.id, // lub tag jeśli masz encję
                        asset.purchasePrice.multiply(asset.quantity).sum().coalesce(BigDecimal.ZERO)
                ))
                .from(asset)
                .where(asset.ownerId.eq(ownerId))
                .groupBy(asset.id) // zmienić na tag w encji AssetTag
                .fetch();
    }

    // ===================== CATEGORY TREND =====================
    public List<StatsCategoryTrendItem> getCategoryTrend(Long ownerId, String category) {
        QAssetEntity asset = QAssetEntity.assetEntity;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(asset.ownerId.eq(ownerId));
        if (category != null) builder.and(asset.category.eq(category));

        return queryFactory
                .select(Projections.constructor(
                        StatsCategoryTrendItem.class,
                        asset.category,
                        asset.createdAt.as("createdDate"),
                        asset.purchasePrice.multiply(asset.quantity).sum().coalesce(BigDecimal.ZERO)
                ))
                .from(asset)
                .where(builder)
                .groupBy(asset.category, asset.createdAt)
                .orderBy(asset.createdAt.asc())
                .fetch();
    }
}

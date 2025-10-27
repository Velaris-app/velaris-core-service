package com.velaris.core.mapper;

import com.velaris.api.model.*;
import com.velaris.core.entity.projection.*;
import com.velaris.core.entity.view.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    TrendDiffItem toDto(StatsTrendDiffView p);

    CategoryItem toDto(StatsCategoryProjection p);

    OverviewItem toDto(StatsOverviewProjection p);

    TrendItem toDto(StatsTrendProjection p);

    TopHoldingItem toDto(StatsTopHoldingsProjection p);

    @Mapping(source = "p.tagName", target = "tag")
    TagItem toDto(StatsTagProjection p);

    CategoryTrendItem toDto(StatsCategoryTrendProjection p);
}

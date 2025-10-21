package com.velaris.core.mapper;

import com.velaris.api.model.*;
import com.velaris.core.entity.view.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    StatsCategoryItem toDto(StatsCategoryView entity);
    StatsOverview toDto(StatsOverviewView entity);
    StatsTrendItem toDto(StatsTrendView entity);
    StatsTagItem toDto(StatsTagView entity);

    StatsTrendDiffItem toDto(StatsTrendDiffView entity);
    StatsTopMoversItem toDto(StatsTopMoversView entity);
    StatsCategoryTrendItem toDto(StatsCategoryTrendView entity);
}

package com.velaris.core.mapper;

import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.entity.RecentActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecentActivitiesMapper {

    @Mapping(target = "assetId", source = "asset.id")
    @Mapping(target = "changeDate", source = "createdAt")
    @Mapping(target = "changedFields", ignore = true)
    RecentActivitiesItem toDto(RecentActivityEntity entity);
}

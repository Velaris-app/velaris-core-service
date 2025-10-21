package com.velaris.core.mapper;

import com.velaris.api.model.RecentActivitiesItem;
import com.velaris.core.entity.view.RecentActivitiesView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecentActivitiesMapper {

    RecentActivitiesItem toDto(RecentActivitiesView entity);
}

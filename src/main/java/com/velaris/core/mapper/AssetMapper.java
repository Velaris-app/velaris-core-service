package com.velaris.core.mapper;

import com.velaris.api.model.Asset;
import com.velaris.core.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(source = "year", target = "purchaseYear")
    @Mapping(target = "ownerId", ignore = true)
    AssetEntity toEntity(Asset dto);

    @Mapping(source = "purchaseYear", target = "year")
    Asset toDto(AssetEntity entity);

    @Mapping(source = "year", target = "purchaseYear")
    @Mapping(target = "ownerId", ignore = true)
    void updateEntity(Asset assetDto, @MappingTarget AssetEntity existing);
}
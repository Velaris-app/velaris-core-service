package com.velaris.core.mapper;

import com.velaris.api.model.Asset;
import com.velaris.core.domain.AssetSnapshot;
import com.velaris.core.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(source = "year", target = "purchaseYear")
    @Mapping(target = "owner", ignore = true)
    AssetEntity toEntity(Asset dto);

    @Mapping(source = "purchaseYear", target = "year")
    Asset toDto(AssetEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "year", target = "purchaseYear")
    @Mapping(target = "owner", ignore = true)
    void updateEntity(Asset assetDto, @MappingTarget AssetEntity existing);

    @Mapping(target = "ownerId", source = "owner.id")
    AssetSnapshot toSnapshot(AssetEntity entity);
}
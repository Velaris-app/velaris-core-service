package com.velaris.core.mapper;

import com.velaris.api.model.SessionItem;
import com.velaris.core.entity.UserSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    @Mapping(target = "deviceInfo", source = "device")
    @Mapping(target = "expiredAt", source = "expiresAt")
    SessionItem toDto(UserSessionEntity entity);
}

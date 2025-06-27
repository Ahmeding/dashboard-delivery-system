package com.hitecart.deliverysystem.mapper.mission;

import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.dto.mission.MissionStartEndRequest;
import com.hitecart.deliverysystem.model.Mission;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {},unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MissionStartEndMapper {

    Mission toEntity(MissionStartEndRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    MissionResponse toResponse(Mission entity);

}

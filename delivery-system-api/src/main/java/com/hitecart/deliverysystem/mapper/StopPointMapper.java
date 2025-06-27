package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.StopPoint;
import com.hitecart.deliverysystem.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {MissionMapper.class},
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface StopPointMapper {

    //@Mapping(target = "mission", source= "missionId")
    @Mapping(target = "mission", ignore = true)
    StopPoint toEntity(StopPointRequest request);

    @AfterMapping
    default void setMissionFromId(StopPointRequest request, @MappingTarget StopPoint stopPoint) {
        if(request.missionId() != null) {
            Mission mission = new Mission();
            mission.setId(request.missionId());
            stopPoint.setMission(mission);
        }
    }
    @Mapping(target = "missionId", source= "mission.id")
    StopPointResponse toResponse(StopPoint entity);

    default Mission mapMission(Integer missionId) {
        if(missionId == null) return null;
        return Mission.builder().id(missionId).build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStopPointFromRequest(StopPointRequest request, @MappingTarget StopPoint stopPoint);
}

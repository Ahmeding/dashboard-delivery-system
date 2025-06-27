package com.hitecart.deliverysystem.mapper.mission;

import com.hitecart.deliverysystem.dto.mission.MissionAssignmentRequest;
import com.hitecart.deliverysystem.dto.mission.MissionResponse;
import com.hitecart.deliverysystem.mapper.MissionVehicleMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionVehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        MissionVehicleMapper.class
},unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MissionAssignmentMapper {
    @Mapping(target = "missionVehicles", ignore = true) // Handled in @AfterMapping
    Mission toEntity(MissionAssignmentRequest missionAssignmentRequest);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    MissionResponse toResponse(Mission entity);


    @AfterMapping
    default void linkChildrenToMission(@MappingTarget Mission mission, MissionAssignmentRequest missionAssignmentRequest,
                                       @Context MissionVehicleMapper missionVehicleMapper) {

        // Link MissionVehicles
        if (missionAssignmentRequest.missionVehicles() != null) {
            mission.setMissionVehicles(missionAssignmentRequest.missionVehicles().stream()
                    .map(vehicleRequest -> {
                        MissionVehicle vehicle = missionVehicleMapper.toEntity(vehicleRequest);
                        vehicle.setMission(mission);
                        return vehicle;
                    })
                    .toList());
        }
    }
}

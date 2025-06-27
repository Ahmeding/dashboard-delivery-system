package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionVehicle;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import com.hitecart.deliverysystem.model.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        MissionMapper.class,
        VehicleMapper.class,
        DriverVehicleMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MissionVehicleMapper {

    @Mapping(target = "driverVehicles", source= "driverVehicles")
    @Mapping(target = "vehicle", source= "vehicleId")
    @Mapping(target = "mission", source= "missionId")
    @Mapping(target = "assignedAt", source = "assignedAt")
    MissionVehicle toEntity(MissionVehicleRequest request);

    @AfterMapping
    default void linkDriverVehicles(@MappingTarget MissionVehicle
                                                missionVehicle) {
        if (missionVehicle.getDriverVehicles() != null) {
            missionVehicle.getDriverVehicles()
                    .forEach(dv->{
                        if(dv != null) {
                            dv.setMissionVehicle(missionVehicle);
                        }
                    });
        }
    }

    @Mapping(target = "driverVehicles", source= "driverVehicles")
    @Mapping(target = "vehicle", source= "vehicle")
    @Mapping(target = "missionId", source= "mission.id")
    MissionVehicleResponse toResponse(MissionVehicle entity);

    default Mission mapMission(Integer missionId) {
        if (missionId == null) return null;
        return Mission.builder().id(missionId).build();
    }

    default Vehicle mapVehicle(Integer vehicleId) {
        if (vehicleId == null) return null;
        return Vehicle.builder().id(vehicleId).build();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMissionVehicleFromRequest(MissionVehicleRequest request, @MappingTarget MissionVehicle missionVehicle);

}

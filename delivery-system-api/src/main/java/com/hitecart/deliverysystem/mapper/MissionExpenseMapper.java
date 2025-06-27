package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.mapper.mission.MissionMapper;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionExpense;
import com.hitecart.deliverysystem.model.MissionVehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        MissionMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MissionExpenseMapper {

    @Mapping(target = "mission", source = "missionId")
    MissionExpense toEntity(MissionExpenseRequest request);
    @Mapping(target = "mission", source = "missionId")
    MissionExpense toEntity(MissionExpenseResponse response);

    @Mapping(target = "missionId", source = "mission.id")
    MissionExpenseResponse toResponse(MissionExpense entity);

    default Mission mapMission(Integer missionId) {
        if (missionId == null) return null;
        return Mission.builder().id(missionId).build();
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMissionExpenseFromRequest(MissionExpenseRequest request, @MappingTarget MissionExpense missionExpense);
}

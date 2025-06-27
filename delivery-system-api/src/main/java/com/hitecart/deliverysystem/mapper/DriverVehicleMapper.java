package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleResponse;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.DriverVehicle;
import com.hitecart.deliverysystem.model.EmploymentContract;
import com.hitecart.deliverysystem.model.MissionVehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {DriverMapper.class,
                MissionVehicleMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverVehicleMapper {

    @Mapping(target = "driver", source = "driverId")
    @Mapping(target = "missionVehicle", source = "missionVehicleId")
    DriverVehicle toEntity(DriverVehicleRequest request);

    @Mapping(target = "driver", source = "driver")
    @Mapping(target = "missionVehicleId", source = "missionVehicle.id")
    DriverVehicleResponse toResponse(DriverVehicle entity);


    default Driver mapDriver(Integer driverId){
        if(driverId == null) return null;
        return Driver.builder().id(driverId).build();
    }
    // Custom ID → Entity methods
    default MissionVehicle mapMissionVehicle(Integer missionVehicleId){
        if(missionVehicleId == null) return null;
        return MissionVehicle.builder().id(missionVehicleId).build();
    }



    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDriverVehicleFromRequest(DriverVehicleRequest request, @MappingTarget DriverVehicle entity);
}

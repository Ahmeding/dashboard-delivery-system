package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driverstats.DriverStatsRequest;
import com.hitecart.deliverysystem.dto.driverstats.DriverStatsResponse;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.DriverStats;
import com.hitecart.deliverysystem.model.DriverVehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {/*DriverMapper.class*/},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverStatsMapper {

    @Mapping(target = "driver" , source = "driverId")
    DriverStats toEntity(DriverStatsRequest request);

    @Mapping(target = "driverId" , source ="driver.id")
    DriverStatsResponse toResponse(DriverStats entity);

    default Driver mapToDriver(Integer id){
        if(id == null) return null;
        return Driver.builder().id(id).build();
    }
    // Add this method
    default Integer mapDriverToId(Driver driver) {
        return driver != null ? driver.getId() : null;
    }




    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDriverStatsFromRequest(DriverStatsRequest request, @MappingTarget DriverStats entity);
}

package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
       // RepairMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface VehicleMapper {

    Vehicle toEntity(VehicleRequest request);

    @Mapping(target = "repairs", source ="repairs")
    VehicleResponse toResponse(Vehicle entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVehicleFromRequest(VehicleRequest request, @MappingTarget Vehicle vehicle);
}

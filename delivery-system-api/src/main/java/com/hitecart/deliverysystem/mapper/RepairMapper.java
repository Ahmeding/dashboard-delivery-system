package com.hitecart.deliverysystem.mapper;


import com.hitecart.deliverysystem.dto.repair.RepairRequest;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.model.Repair;
import com.hitecart.deliverysystem.model.Repair;
import com.hitecart.deliverysystem.model.SparePart;
import com.hitecart.deliverysystem.model.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        VehicleMapper.class,
        //SparePartMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RepairMapper {

    @Mapping(target="spareParts" , source ="spareParts")
    @Mapping(target="vehicle" , source ="vehicleId")
    Repair toEntity(RepairRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target="spareParts" , source ="spareParts")
    @Mapping(target="vehicleId" , source ="vehicle.id")
    RepairResponse toResponse(Repair entity);

    default Vehicle mapVehicle(Integer vehicleId) {
        if (vehicleId == null) return null;
        return Vehicle.builder().id(vehicleId).build();
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRepairFromRequest(RepairRequest request, @MappingTarget Repair repair);
}

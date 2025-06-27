package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.sparepart.SparePartRequest;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.model.Repair;
import com.hitecart.deliverysystem.model.SparePart;
import com.hitecart.deliverysystem.model.StopPoint;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        RepairMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SparePartMapper {

    @Mapping(target = "repair", source = "repairId")
    SparePart toEntity(SparePartRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target = "repairId", source = "repair.id")
    SparePartResponse toResponse(SparePart entity);

    default Repair mapRepair(Integer repairId) {
        if(repairId == null) return null;
        return Repair.builder().id(repairId).build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSparePartFromRequest(SparePartRequest request, @MappingTarget SparePart sparePart);
}

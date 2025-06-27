package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.EmploymentContract;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EmploymentContractMapper {

    @Mapping(target = "driver",source ="driverId")
    EmploymentContract toEntity(EmploymentContractRequest request);

    @Mapping(target = "driverId",source ="driver.id")
    EmploymentContractResponse toResponse(EmploymentContract entity);
    // Map driverId to Driver entity

    default Driver mapDriver(Integer driverId) {
        if (driverId == null) return null;
        Driver driver = new Driver();
                driver.setId(driverId);
        return driver;
    }
    // Optional: Add method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(EmploymentContractRequest request, @MappingTarget EmploymentContract entity);
}

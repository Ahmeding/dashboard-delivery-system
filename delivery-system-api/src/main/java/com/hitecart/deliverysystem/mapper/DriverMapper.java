package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.driver.DriverUpdateRequest;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractRequest;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.DriverVehicle;
import com.hitecart.deliverysystem.model.EmploymentContract;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {

/*                DriverVehicleMapper.class,
                RoleMapper.class,
                DriverStatsMapper.class */},
                unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverMapper {

    @Mapping(target = "address", source = "address")
    @Mapping(target = "employmentContracts", source = "employmentContracts")
    @Mapping(target = "assignments", source = "assignments")
    @Mapping(target = "roles", source = "roles")
    Driver toEntity(DriverRequest request);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "employmentContractsIds", source = "employmentContracts") // id -> object
    @Mapping(target = "assignmentIds", source = "assignments")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "stats", source = "stats")
    @Mapping(target = "rate" , expression = "java(entity.getRate())")
    DriverResponse toResponse(Driver entity);

    default List<Integer> mapEmploymentContractsIds(List<EmploymentContract> employmentContracts) {
        return employmentContracts == null ? null : employmentContracts.stream()
                .map(EmploymentContract::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapAssignmentIds(List<DriverVehicle> assignments) {
        return assignments == null ? null:assignments.stream()
                .map(DriverVehicle::getId)
                .collect(Collectors.toList());
    }

    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDriverFromRequest(DriverUpdateRequest request, @MappingTarget Driver entity);
}

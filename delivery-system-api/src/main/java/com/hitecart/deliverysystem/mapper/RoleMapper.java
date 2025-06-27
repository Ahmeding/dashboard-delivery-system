package com.hitecart.deliverysystem.mapper;


import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import com.hitecart.deliverysystem.dto.role.RoleUpdateRequest;
import com.hitecart.deliverysystem.model.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RoleMapper {

    Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromRequest(RoleUpdateRequest request, @MappingTarget Role role);
}

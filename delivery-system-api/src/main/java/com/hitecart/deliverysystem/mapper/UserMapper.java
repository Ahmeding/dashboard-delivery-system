package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        AddressMapper.class,
        RoleMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    @Mapping(target = "address", source = "address")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    UserResponse toResponse(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}

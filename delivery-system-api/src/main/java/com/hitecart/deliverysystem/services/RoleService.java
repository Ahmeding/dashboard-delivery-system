package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import com.hitecart.deliverysystem.dto.role.RoleUpdateRequest;

import java.util.List;

public interface RoleService {
    RoleResponse save(RoleRequest dto);

    RoleResponse updateRoleInfo(Integer roleId, RoleUpdateRequest roleUpdateRequest);

    RoleResponse findById(Integer id);

    List<RoleResponse> findAll();

    void delete(Integer id);
}

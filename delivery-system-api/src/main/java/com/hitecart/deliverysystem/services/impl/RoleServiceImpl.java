package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import com.hitecart.deliverysystem.dto.role.RoleUpdateRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.RoleMapper;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.services.RoleService;
import com.hitecart.deliverysystem.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;


    @Override
    public RoleResponse save(RoleRequest roleRequest) {
        checkRequestValidating(roleRequest, "Role not valid", "Role not valid", ErrorCodes.ROLE_NOT_VALID);

        return savingRole(roleRequest);
    }

    @Override
    public RoleResponse updateRoleInfo(Integer roleId, RoleUpdateRequest roleUpdateRequest) {
        //checkRequestValidating(roleRequest, "Role not valid", "Role not valid", ErrorCodes.ROLE_NOT_VALID);
        var existingRole = roleRepository.findById(roleId).orElseThrow(
                () -> new EntityNotFoundException("Role with ::ID : " + roleId + "not found.", ErrorCodes.ROLE_NOT_FOUND)
        );

        roleMapper.updateRoleFromRequest(roleUpdateRequest, existingRole);

        Role updatedRole = roleRepository.save(existingRole);

        return roleMapper.toResponse(updatedRole);
    }

    private RoleResponse savingRole(RoleRequest roleRequest) {
        Role roleToSave = roleMapper.toEntity(roleRequest);
        return roleMapper.toResponse(roleRepository.save(roleToSave));
    }

    private void checkRequestValidating(RoleRequest roleRequest, String logMsg, String exceptionMsg, ErrorCodes errorCodes) {
        List<String> errors = RoleValidator.validate(roleRequest);
        if (!errors.isEmpty()) {
            log.error(logMsg, roleRequest);
            throw new InvalidEntityException(exceptionMsg, errorCodes, errors);
        }
    }

    @Override
    public RoleResponse findById(Integer id) {
        return null;
    }

    @Override
    public List<RoleResponse> findAll() {
        return List.of();
    }

    @Override
    public void delete(Integer id) {
        if(id == null || id <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.ROLE_NOT_VALID_OPERATION);
        }
        this.findById(id);
        List<User> users = userRepository.findAllByRoleId(id);
        if(!users.isEmpty()) {
            throw new InvalidOperationException("Role with ::ID : " + id + "assigned to many users", ErrorCodes.ROLE_NOT_VALID_OPERATION);
        }
        roleRepository.deleteById(id);
    }
}

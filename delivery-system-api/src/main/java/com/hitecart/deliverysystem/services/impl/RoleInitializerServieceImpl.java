package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.IRoleInitializerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleInitializerServieceImpl implements IRoleInitializerService {
    private final RoleRepository roleRepository;

    @Override
    public void init() {
        createRoleIfNotFound(RoleType.ROLE_ADMIN);
        createRoleIfNotFound(RoleType.ROLE_USER);
        createRoleIfNotFound(RoleType.ROLE_DRIVER);
    }

    private void createRoleIfNotFound(RoleType roleName) {
        if(!roleRepository.existsByName(roleName.name())){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}

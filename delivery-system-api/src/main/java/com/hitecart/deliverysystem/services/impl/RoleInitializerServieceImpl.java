package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.enums.RoleName;
import com.hitecart.deliverysystem.services.IRoleInitializerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleInitializerServieceImpl implements IRoleInitializerService {
    private final RoleRepository roleRepository;

    @Override
    public void init() {
        createRoleIfNotFound(RoleName.ROLE_ADMIN.name());
        createRoleIfNotFound(RoleName.ROLE_USER.name());
        createRoleIfNotFound(RoleName.ROLE_DRIVER.name());
    }

    private void createRoleIfNotFound(String roleName) {
        if(!roleRepository.existsByName(roleName)){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}

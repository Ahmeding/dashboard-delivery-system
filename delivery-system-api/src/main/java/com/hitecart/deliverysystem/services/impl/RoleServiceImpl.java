package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import com.hitecart.deliverysystem.services.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;


    @Override
    public RoleDto save(RoleDto dto) {

        return null;
    }

    @Override
    public RoleDto findById(Integer id) {
        return null;
    }

    @Override
    public List<RoleDto> findAll() {
        return List.of();
    }

    @Override
    public void delete(Integer id) {

    }
}

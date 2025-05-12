package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ReparationApi;
import com.hitecart.deliverysystem.controllers.api.RoleApi;
import com.hitecart.deliverysystem.dto.basic.ReparationDto;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import com.hitecart.deliverysystem.services.IRoleService;
import com.hitecart.deliverysystem.services.ReparationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class RoleController implements RoleApi {

    private final IRoleService roleService;


    @Override
    public ResponseEntity<RoleDto> save(RoleDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<RoleDto> findById(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<RoleDto>> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}

package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRoleService {
    public RoleDto save(RoleDto dto) ;
    public RoleDto findById(Integer id);
    List<RoleDto> findAll();
    public void delete(Integer id);
}

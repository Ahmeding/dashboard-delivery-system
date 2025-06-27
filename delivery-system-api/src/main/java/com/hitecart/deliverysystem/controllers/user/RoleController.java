package com.hitecart.deliverysystem.controllers.user;

import com.hitecart.deliverysystem.controllers.api.RoleApi;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import com.hitecart.deliverysystem.dto.role.RoleUpdateRequest;
import com.hitecart.deliverysystem.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class RoleController implements RoleApi {

    private final RoleService roleService;


    @Override
    public ResponseEntity<RoleResponse> saveRole(RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.save(roleRequest));
    }

    @Override
    public ResponseEntity<RoleResponse> updateRoleInfo(Integer roleId, RoleUpdateRequest roleUpdateRequest) {
        return ResponseEntity.ok(roleService.updateRoleInfo(roleId, roleUpdateRequest));
    }

    @Override
    public ResponseEntity<RoleResponse> findRoleById(Integer id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @Override
    public ResponseEntity<List<RoleResponse>> findAllRole() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteRole(Integer id) {
        try {
            roleService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Role deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Role not found with ID: " + id
                    ));
        }
    }
}

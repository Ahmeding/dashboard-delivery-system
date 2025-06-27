package com.hitecart.deliverysystem.dto.role;

import com.hitecart.deliverysystem.model.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record RoleRequest(
        Integer id,
         @NotBlank @NotNull(message = "Role name is required")
         @Size(max = 50, message = "Role name must be ≤50 characters")
         RoleType name
) {
}

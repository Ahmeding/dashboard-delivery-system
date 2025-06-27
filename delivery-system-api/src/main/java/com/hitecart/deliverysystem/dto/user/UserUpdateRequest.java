package com.hitecart.deliverysystem.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record UserUpdateRequest(
        @NotBlank(message = "First name is required")  // Not null/empty/whitespace
        @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
        String firstname,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
        String lastname,

        @Pattern(regexp = "^\\+?[0-9\\s-]{8,}$", message = "Invalid phone number")
        String phoneNbr,

        @Email(message = "Invalid email format")  // Validates email@domain format
        @NotBlank(message = "Email is required")
        String email,

        @Valid  // Triggers validation for nested AddressRequest
        AddressRequest address,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        @Past(message = "Birth date must be in the past")  // Ensure date is not future
        LocalDate birthDate,

        boolean accountLocked,
        boolean enabled,

        @Valid  // Validate each RoleRequest in the list
        List<RoleRequest> roles
) {}

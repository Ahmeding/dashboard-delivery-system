package com.hitecart.deliverysystem.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.address.AddressResponse;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record UserRequest(

        Integer id,

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
        String firstname,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be 2–50 characters")
        String lastname,

        @Pattern(
                regexp = "^\\+?[0-9\\s-]{8,}$",
                message = "Invalid phone number format"
        )
        String phoneNbr,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @Valid
        AddressRequest address,

        @NotNull(message = "Birth date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        boolean accountLocked,
        boolean enabled,

        @Valid
        @NotEmpty(message = "At least one role must be provided")
        List<@Valid RoleRequest> roles

) {
}

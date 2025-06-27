package com.hitecart.deliverysystem.dto.deliverypointcontact;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DeliveryPointContactRequest(
        @Schema(
                description = "Contact firstname",
                example = "John",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be 2–50 characters long")
        String firstname,


        @Schema(
                description = "Contact lastname",
                example = "Doe",
                minLength = 2,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be 2–50 characters long")
        String lastname,


        @Schema(
                description = "Contact Phone number",
                example = "+33745455468",
                minLength = 12,
                maxLength = 12,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+?[0-9\\s-]{8,}$",
                message = "Invalid phone number format"
        )
        String phoneNbr,


        @Schema(
                description = "Contact email",
                example = "john.doe@g.c",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email

) {}

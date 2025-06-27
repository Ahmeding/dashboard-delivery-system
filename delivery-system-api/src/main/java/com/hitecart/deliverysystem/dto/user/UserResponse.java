package com.hitecart.deliverysystem.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressResponse;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String phoneNbr;
    private String email;
    private AddressResponse address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate birthDate;
    private boolean accountLocked;
    private boolean enabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private List<RoleResponse> roles;

}

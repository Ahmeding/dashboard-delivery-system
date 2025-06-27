package com.hitecart.deliverysystem.dto.driver;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressResponse;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleResponse;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import com.hitecart.deliverysystem.model.DriverStats;
import com.hitecart.deliverysystem.model.enums.DriverExperience;
import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverResponse {

    @Schema(description = "Unique user identifier", example = "12345")
    private Integer id;

    @Schema(description = "User's family name", example = "Dupont")
    private String lastname;

    @Schema(description = "User's given name", example = "Marie")
    private String firstname;

    @Schema(description = "Contact phone number", example = "+33 6 12 34 56 78")
    private String phoneNbr;

    @Schema(description = "Verified email address", example = "marie.dupont@example.com")
    private String email;

    @Schema(description = "Experience classification")
    private DriverExperience experienceLevel;

    private AddressResponse address;
    private boolean accountLocked;
    private boolean enabled;
    //Driver
    private String drivinglicenseNbr;
    private DrivingLicenseType drivinglicenseType;
    private String secuNbr;
    private String rib;

    private boolean isAssigned;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime nextAvailableTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;

    private Integer createdBy;
    private Integer lastModifiedBy;

    private DriverStats stats;

    private List<Integer> employmentContractsIds;
    private List<Integer> assignmentIds;
    private List<RoleResponse> roles;

}


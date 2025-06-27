package com.hitecart.deliverysystem.dto.mission;

import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.model.enums.AssignmentStatus;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record MissionAssignmentRequest(
        Integer id,

        @NotNull(message = "Volume is required")
        @DecimalMin(value = "0", inclusive = true, message = "Volume must be zero or positive")
        BigDecimal volume,

        @NotNull(message = "Weight is required")
        @DecimalMin(value = "0", inclusive = true, message = "Weight must be zero or positive")
        BigDecimal weight,

        @NotNull(message = "Assignment status is required")
        AssignmentStatus assignmentStatus,

        @NotNull(message = "Mission status is required")
        MissionStatus missionStatus,

        @NotEmpty(message = "At least one mission vehicle is required")
        @Valid
        List<MissionVehicleRequest> missionVehicles

) {}

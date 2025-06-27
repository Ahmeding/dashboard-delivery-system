package com.hitecart.deliverysystem.dto.missionvehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record MissionVehicleRequest(
        Integer id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
        LocalDateTime assignedAt,

        @Valid
        @NotEmpty(message = "At least one driver vehicle assignment is required")
        List<DriverVehicleRequest> driverVehicles,

        @NotNull(message = "Mission ID is required")
        @Positive(message = "Mission ID must be positive")
        Integer missionId,

        @NotNull(message = "Vehicle ID is required")
        @Positive(message = "Vehicle ID must be positive")
        Integer vehicleId
){}

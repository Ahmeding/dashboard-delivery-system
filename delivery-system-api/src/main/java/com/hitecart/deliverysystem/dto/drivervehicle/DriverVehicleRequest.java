package com.hitecart.deliverysystem.dto.drivervehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.model.MissionVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record DriverVehicleRequest(
        Integer id,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T08:30")
        LocalDateTime assignedAt,

        @NotNull(message = "Driver ID is required")
        @Positive(message = "Driver ID must be positive")
        Integer driverId,

        @NotNull(message = "Mission Vehicle ID is required")
        @Positive(message = "Mission Vehicle ID must be positive")
        Integer missionVehicleId
)
{}

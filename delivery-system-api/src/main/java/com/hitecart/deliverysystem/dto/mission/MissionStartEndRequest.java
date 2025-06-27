package com.hitecart.deliverysystem.dto.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record MissionStartEndRequest(

        @NotNull(message = "Mission ID is required")
        Integer id,

        @NotNull(message = "Vehicle ID is required")
        Integer vehicleId,

        @NotNull(message = "Mission status is required")
        MissionStatus missionStatus,

        @NotNull(message = "Start mileage is required")
        @DecimalMin(value = "0", inclusive = true, message = "Start mileage must be zero or positive")
        Double startMileage,


        @Future(message = "Eta cannot be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
        LocalDateTime eta,

        @NotNull(message = "End mileage is required")
        @DecimalMin(value = "0", inclusive = true, message = "End mileage must be zero or positive")
        Double endMileage,

        @NotNull(message = "Kilometer flag is required")
        KmFlag flag

)
{}

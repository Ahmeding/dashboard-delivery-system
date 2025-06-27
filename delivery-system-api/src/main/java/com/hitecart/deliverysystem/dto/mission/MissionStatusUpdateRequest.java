package com.hitecart.deliverysystem.dto.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.KmFlag;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MissionStatusUpdateRequest(

        @NotNull(message = "Mission ID is required")
        Integer id,
        @NotNull(message = "Mission status is required")
        MissionStatus missionStatus

)
{}

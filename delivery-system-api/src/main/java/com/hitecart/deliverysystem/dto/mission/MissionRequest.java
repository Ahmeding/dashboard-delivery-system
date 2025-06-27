package com.hitecart.deliverysystem.dto.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.missionexpense.MissionExpenseRequest;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleRequest;
import com.hitecart.deliverysystem.dto.stoppoint.StopPointRequest;
import com.hitecart.deliverysystem.model.enums.AssignmentStatus;
import com.hitecart.deliverysystem.model.enums.DeliveryPointType;
import com.hitecart.deliverysystem.model.enums.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record MissionRequest(


        Integer id,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be at most 100 characters")
        @Schema(
                description = "Mission Title",
                example = "Mission Lyon",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String title,

        @Size(max = 500, message = "Comment must be at most 500 characters")
        @Schema(
                description = "Mission Description",
                example = "Mission Lyon desc ...",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String comment,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date cannot be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
        LocalDateTime endDate,

        @NotNull(message = "Mission status is required")
        @Schema(
                description = "Mission Status",
                implementation = MissionStatus.class,
                example = "PLANNED",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MissionStatus missionStatus,

        @Schema(
                description = "Mission : return Transport Required",
                example = "false"
        )
        boolean returnTransportRequired,
        @NotEmpty(message = "At least one stop point is required")
        @Valid
        @Schema(
                description = "Stop Point list ",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        List<StopPointRequest> stopPoints,

        @NotNull(message = "Delivery destination ID is required")
        @Schema(
                description = "Delivery Destination ID",
                example = "2",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        Integer deliveryDestinationId

) {

}

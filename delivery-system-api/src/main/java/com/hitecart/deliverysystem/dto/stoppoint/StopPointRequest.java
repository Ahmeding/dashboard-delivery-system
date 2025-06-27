package com.hitecart.deliverysystem.dto.stoppoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record StopPointRequest(

        Integer id,

        @Schema(type = "string",
                example = "Lv - Lyon",
                description = "Title",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String title,
        @Size(max = 255, message = "Comment cannot exceed 255 characters")
        @Schema(type = "string",
                example = "description...",
                description = "Comment",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String comment,

        @NotNull(message = "Scheduled delivery time is required")
        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string",
                format = "time",
                example = "08:30",
                description = "Scheduled Delivery Time",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        LocalTime scheduledDeliveryTime,

        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string",
                format = "time",
                example = "08:12",
                description = "Actual Delivery Time",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        LocalTime actualDeliveryTime,

        @NotNull(message = "Departure date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Departure date cannot be in the past")
        @Schema(type = "string",
                format = "date",
                example = "2025-05-27",
                description = "Departure date",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        LocalDate departureDate,

        @NotNull(message = "Arrival date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Arrival Date cannot be in the past")
        @Schema(type = "string", format = "date", example = "2025-05-27")
        LocalDate arrivalDate,

        @NotBlank(message = "Departure location is required")
        @Schema(
                description = "Departure Location",
                example = "Paris",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String departureLocation,

        @NotBlank(message = "Arrival location is required")
        @Schema(
                description = "Arrival location",
                example = "Lyon",
                accessMode = Schema.AccessMode.READ_WRITE
        )
        String arrivalLocation,

        @NotNull(message = "Mission ID is required")
        Integer missionId
) {
}

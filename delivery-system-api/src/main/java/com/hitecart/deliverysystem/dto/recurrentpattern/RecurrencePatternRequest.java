package com.hitecart.deliverysystem.dto.recurrentpattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.RecurrenceInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public record RecurrencePatternRequest (

        @NotNull(message = "Recurrence interval is required")
        RecurrenceInterval recurrenceInterval,

        @NotEmpty(message = "At least one delivery day of week must be provided")
        Set<@NotNull DayOfWeek> daysOfWeek,

        @NotNull(message = "Start date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-06-01")
        @FutureOrPresent(message = "Start date must be today or in the future")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(type = "string", format = "date", example = "2025-12-31")
        @FutureOrPresent(message = "End date must be today or in the future")
        LocalDate endDate
){}

package com.hitecart.deliverysystem.dto.recurrentpattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import com.hitecart.deliverysystem.model.enums.RecurrenceInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecurrencePatternResponse {
    private RecurrenceInterval recurrenceInterval;
    private Set<DayOfWeek> daysOfWeek; // Delivery days (e.g. MONDAY, FRIDAY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate startDate; // First delivery date
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate endDate;   // Optional end date
}

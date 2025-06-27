package com.hitecart.deliverysystem.dto.stoppoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StopPointResponse {

    private Integer id;
    private String title;
    private String comment;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduledDeliveryTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime actualDeliveryTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate departureDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate arrivalDate;
    private String departureLocation;
    private String arrivalLocation;
    private Integer missionId;
}

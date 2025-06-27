package com.hitecart.deliverysystem.dto.driverstats;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverStatsResponse {
       @Schema(description = "Unique stats identifier", example = "789")
       private Integer id;
       @Schema(description = "Long distance trip count", example = "12")
       private int longTrips;
       @Schema(description = "Standard trip count", example = "45")
       private int standardTrips;

       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
       @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
       private LocalDateTime createdAt;

       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
       @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
       private LocalDateTime updatedAt;


       @Schema(description = "Driver id", example = "456")
       private Integer driverId;
}

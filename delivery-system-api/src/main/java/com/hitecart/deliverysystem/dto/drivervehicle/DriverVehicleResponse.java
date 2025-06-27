package com.hitecart.deliverysystem.dto.drivervehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.missionvehicle.MissionVehicleResponse;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.MissionVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverVehicleResponse {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
    private LocalDateTime assignedAt;
    // private Integer driverId;
    private DriverResponse driver;
    private Integer missionVehicleId;

}

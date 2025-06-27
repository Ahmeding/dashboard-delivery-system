package com.hitecart.deliverysystem.dto.missionvehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.drivervehicle.DriverVehicleResponse;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.model.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionVehicleResponse {

    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00")
    private LocalDateTime assignedAt;

    private List<DriverVehicleResponse> driverVehicles;
    private Integer missionId;
    private VehicleResponse vehicle;
    private Integer vehicleId;
}

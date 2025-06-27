package com.hitecart.deliverysystem.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.repair.RepairResponse;
import com.hitecart.deliverysystem.model.enums.EnergyType;
import com.hitecart.deliverysystem.model.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleResponse {

    private Integer id;
    private String reference;
    private String logo;
    private VehicleType vehicleType;
    private String registrationNbr;
    private String brand;
    private String model;
    private String vinNbr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate dfcDate;
    private BigDecimal mileage;
    private EnergyType energyType;
    private BigDecimal weightCapacity;
    private BigDecimal volumeCapacity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate ctExpDate;
    private Boolean isActive;
    private boolean isAssigned;
    private boolean isAvailable;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

    private List<RepairResponse> repairs;
}

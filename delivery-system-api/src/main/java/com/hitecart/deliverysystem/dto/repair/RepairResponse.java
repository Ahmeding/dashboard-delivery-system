package com.hitecart.deliverysystem.dto.repair;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.sparepart.SparePartResponse;
import com.hitecart.deliverysystem.dto.vehicle.VehicleResponse;
import com.hitecart.deliverysystem.model.enums.TypeReparation;
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
public class RepairResponse {

    private Integer id;
    private String reference;
    private TypeReparation type;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate nextRepairDate;
    private BigDecimal cost;

    //private VehicleResponse vehicle;
    private Integer vehicleId;
    private List<SparePartResponse> spareParts;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;

}

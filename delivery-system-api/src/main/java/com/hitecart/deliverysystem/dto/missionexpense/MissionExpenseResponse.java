package com.hitecart.deliverysystem.dto.missionexpense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.ExpensesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionExpenseResponse {


    @Schema(description = "Unique identifier for the expense")
    private  Integer id;

    @Schema(description = "Reference code for the expense")
    private  String reference;

    @Schema(description = "Name or title of the expense")
    private  String name;

    @Schema(description = "Category or type of the expense (e.g., FUEL, TOLL, ACCOMMODATION)")
    private  ExpensesType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00",
            description = "Date and time when the expense occurred")
    private  LocalDateTime expenseDate;

    @Schema(description = "Quantity or amount of units for the expense (e.g., number of liters for fuel)")
    private  BigDecimal quantity;

    @Schema(description = "Cost per unit of the expense")
    private  BigDecimal cost;

    @Schema(description = "Total amount of the expense (quantity × cost)")
    private  BigDecimal totalAmount;  // New calculated field

    @Schema(description = "Additional notes or comments about the expense")
    private  String comment;

    @Schema(description = "ID of the mission this expense belongs to")
    private  Integer missionId;

    // Audit metadata in separate section
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00",
            description = "Timestamp when the expense record was created")
    private  LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00",
            description = "Timestamp when the expense record was last updated")
    private  LocalDateTime updatedAt;

    @Schema(description = "ID of the user who created the expense record")
    private  Integer createdBy;

    @Schema(description = "ID of the user who last modified the expense record")
    private  Integer lastModifiedBy;

}

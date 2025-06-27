package com.hitecart.deliverysystem.dto.employmentcontract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.enums.ContractStatus;
import com.hitecart.deliverysystem.model.enums.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EmploymentContractRequest(
        Integer id,

        @NotNull(message = "Contract type is required")
        @Schema(description = "Type of employment contract", example = "CDD")
        ContractType contractType,
        @NotNull(message = "Contract status is required")
        @Schema(description = "Status of employment contract", example = "Active")
        ContractStatus contractStatus,
        @NotNull(message = "Start date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "Contract start date (YYYY-MM-DD format)", type = "string", format = "date", example = "2025-05-01")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "Contract end date (YYYY-MM-DD format)", type = "string", format = "date", example = "2025-05-14")
        LocalDate endDate,

        @NotNull(message = "Driver ID is required")
        @Positive(message = "Driver ID must be a positive number")
        @Schema(description = "Associated driver ID", example = "456")
        Integer driverId

) {
    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateValid() {
        return endDate.isAfter(startDate);
    }
}

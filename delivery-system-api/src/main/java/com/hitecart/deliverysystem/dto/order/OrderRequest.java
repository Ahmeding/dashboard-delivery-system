package com.hitecart.deliverysystem.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.orderline.OrderLineRequest;
import com.hitecart.deliverysystem.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record OrderRequest(

        Integer id,
        @NotBlank(message = "Order code is required")
        @Size(max = 50, message = "Order code must be at most 50 characters")
        String code,

        @NotNull(message = "Order date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
        LocalDateTime orderDate,

        @NotNull(message = "Total amount HT is required")
        @DecimalMin(value = "0", inclusive = true, message = "Total amount HT must be zero or positive")
        BigDecimal totalAmountHT,

        @NotNull(message = "Total amount TTC is required")
        @DecimalMin(value = "0", inclusive = true, message = "Total amount TTC must be zero or positive")
        BigDecimal totalAmountTTC,

        @NotNull(message = "Order status is required")
        OrderStatus orderStatus,

        @NotNull(message = "Client ID is required")
        Integer clientId,

        @NotEmpty(message = "At least one order line is required")
        @Valid
        List<OrderLineRequest> orderLines

) {
}

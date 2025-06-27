package com.hitecart.deliverysystem.dto.orderline;

import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderLineRequest(

        Integer id,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0", inclusive = false, message = "Quantity must be greater than zero")
        BigDecimal quantity,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0", inclusive = true, message = "Unit price must be zero or positive")
        BigDecimal unitPrice,

        @NotNull(message = "TTC price is required")
        @DecimalMin(value = "0", inclusive = true, message = "TTC price must be zero or positive")
        BigDecimal ttcPrice,

        @NotNull(message = "TVA (tax) is required")
        @DecimalMin(value = "0", inclusive = true, message = "TVA must be zero or positive")
        BigDecimal tva,

        @NotNull(message = "Delivery service ID is required")
        Integer deliveryServiceId,

        @NotNull(message = "Order ID is required")
        Integer orderId
) {
}

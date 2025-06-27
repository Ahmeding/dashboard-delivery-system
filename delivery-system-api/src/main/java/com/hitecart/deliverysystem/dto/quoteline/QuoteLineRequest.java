package com.hitecart.deliverysystem.dto.quoteline;


import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QuoteLineRequest(
         Integer id,

         @NotNull(message = "Quantity is required")
         @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than zero")
         BigDecimal quantity,

         @NotNull(message = "Unit price is required")
         @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
         BigDecimal unitPrice,

         @NotNull(message = "TTC price is required")
         @DecimalMin(value = "0.0", inclusive = false, message = "TTC price must be greater than zero")
         BigDecimal ttcPrice,

         @NotNull(message = "TVA rate is required")
         @DecimalMin(value = "0.0", inclusive = true, message = "TVA rate cannot be negative")
         BigDecimal tva,

         @NotNull(message = "Delivery service ID is required")
         Integer deliveryServiceId,

         @NotNull(message = "Quote ID is required")
         Integer quoteId
) {
}

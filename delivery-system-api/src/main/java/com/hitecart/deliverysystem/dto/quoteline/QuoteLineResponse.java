package com.hitecart.deliverysystem.dto.quoteline;

import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteLineResponse {

    private Integer id;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal ttcPrice;
    private BigDecimal tva;
    private DeliveryServiceResponse deliveryService;
    private QuoteResponse quote;
}

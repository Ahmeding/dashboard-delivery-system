package com.hitecart.deliverysystem.dto.orderline;

import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineResponse{

        private Integer id;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal ttcPrice;
        private BigDecimal tva;
        private DeliveryServiceResponse deliveryService;
        private OrderResponse order;

}

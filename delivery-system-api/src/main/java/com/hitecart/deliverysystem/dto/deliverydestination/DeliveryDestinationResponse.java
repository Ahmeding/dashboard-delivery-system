package com.hitecart.deliverysystem.dto.deliverydestination;

import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.dto.deliveryservice.DeliveryServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDestinationResponse {
        private Integer id;
        private String reference;
        private DeliveryServiceResponse deliveryService;
        private List<Integer> deliveryPointIds;
}

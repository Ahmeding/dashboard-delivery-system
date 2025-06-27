package com.hitecart.deliverysystem.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.orderline.OrderLineResponse;
import com.hitecart.deliverysystem.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Integer id;
    private String reference;
    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime orderDate;
    private BigDecimal totalAmountHT;
    private BigDecimal totalAmountTTC;
    private OrderStatus orderStatus;
    private Integer clientId;
    private List<OrderLineResponse> orderLines;




    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
}

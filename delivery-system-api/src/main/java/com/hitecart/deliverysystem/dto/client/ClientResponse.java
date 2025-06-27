package com.hitecart.deliverysystem.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.dto.address.AddressRequest;
import com.hitecart.deliverysystem.dto.deliverypoint.DeliveryPointResponse;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import com.hitecart.deliverysystem.dto.role.RoleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {

    private Integer id;
    private String reference;
    private String lastname;
    private String firstname;
    private String phoneNbr;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-27")
    private LocalDate birthDate;
    private String description;
    private AddressRequest address;

    private String siret;
    private String siren;
    private boolean accountLocked;
    private boolean enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    private Integer createdBy;

    private List<RoleResponse> roles;
    private List<NotificationResponse> notifications;
    private List<OrderResponse> orders;
    private List<QuoteResponse> quotes;
    private List<DeliveryPointResponse> deliveryPoints;
}

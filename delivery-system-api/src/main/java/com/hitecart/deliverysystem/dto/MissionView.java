package com.hitecart.deliverysystem.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MissionView {
    Integer getId();
    String getReference();
    LocalDateTime getDateDebut();
    LocalDateTime getDateFin();
    BigDecimal getVolume();

    @Value("#{target.deliveryDestination.serviceDelivery.distance}")
    Double getDistance();
}

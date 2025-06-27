package com.hitecart.deliverysystem.dto.notification;

import com.hitecart.deliverysystem.model.enums.NotificationStatus;

public record NotificationRequest(
        Integer id,
        String title,
        String message,
        NotificationStatus status,
        Integer userId
) {
}

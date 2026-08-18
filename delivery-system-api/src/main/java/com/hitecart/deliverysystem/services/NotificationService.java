package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.notification.NotificationRequest;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendNotification(Integer userId, NotificationRequest request);

    NotificationResponse markAsRead(NotificationRequest dto);

    List<NotificationResponse> findAllByUserId(Integer userId);
}

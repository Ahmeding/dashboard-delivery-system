package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.dto.basic.NotificationDto;
import com.hitecart.deliverysystem.model.Notification;

import java.util.List;

public interface INotificationService {
    public void sendNotification(Integer userId, NotificationDto dto);

    NotificationDto markAsRead(NotificationDto dto);

    List<NotificationDto> findAllByUserId(Integer userId);
}

package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.model.Notification;

public interface INotificationService {
    public void sendNotification(Integer userId, Notification notification);
}

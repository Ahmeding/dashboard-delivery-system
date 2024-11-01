package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.services.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(Integer userId, Notification notification) {
        log.info("sen nottif to {} with payload: {}", userId, notification);
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/notification",
                notification
                );
    }
}

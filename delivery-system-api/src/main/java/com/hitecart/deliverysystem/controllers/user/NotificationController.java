package com.hitecart.deliverysystem.controllers.user;

import com.hitecart.deliverysystem.controllers.api.NotificationApi;
import com.hitecart.deliverysystem.dto.notification.NotificationRequest;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;
import com.hitecart.deliverysystem.services.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final INotificationService notificationService;

    @Override
    public ResponseEntity<NotificationResponse> markAsRead(NotificationRequest notificationRequest) {
        return ResponseEntity.ok(this.notificationService.markAsRead(notificationRequest));
    }

    @Override
    public ResponseEntity<List<NotificationResponse>> findAllByUserID(Integer userId) {
        return ResponseEntity.ok(this.notificationService.findAllByUserId(userId));
    }
}

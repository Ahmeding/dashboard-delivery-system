package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.NotificationApi;
import com.hitecart.deliverysystem.controllers.api.RoleApi;
import com.hitecart.deliverysystem.dto.basic.NotificationDto;
import com.hitecart.deliverysystem.dto.basic.RoleDto;
import com.hitecart.deliverysystem.services.INotificationService;
import com.hitecart.deliverysystem.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final INotificationService notificationService;

    @Override
    public ResponseEntity<NotificationDto> markAsRead(NotificationDto dto) {
        return ResponseEntity.ok(this.notificationService.markAsRead(dto));
    }

    @Override
    public ResponseEntity<List<NotificationDto>> findAllByUserID(Integer userId) {
        return ResponseEntity.ok(this.notificationService.findAllByUserId(userId));
    }
}

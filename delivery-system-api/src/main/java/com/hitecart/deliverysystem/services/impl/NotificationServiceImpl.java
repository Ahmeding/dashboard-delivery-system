package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.INotificationRepository;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.dto.notification.NotificationRequest;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.NotificationMapper;
import com.hitecart.deliverysystem.mapper.UserMapper;
import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.services.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository iNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    public void sendNotification(Integer userId, NotificationRequest notificationRequest) {
        log.info("sen nottif to {} with payload: {}", userId, notificationRequest);

        Notification notification = notificationMapper.toEntity(notificationRequest);

        User reciver = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User Not Found",ErrorCodes.USER_NOT_FOUND)
        );
        notification.setCreatedBy(1);
        notification.setUser(reciver);
        notification.setStatus(NotificationStatus.SEND);

        Notification savedNotification = iNotificationRepository.save(notification);

        // Convert to DTO for WebSocket
        NotificationMessage message = new NotificationMessage(
                savedNotification.getId(),
                savedNotification.getTitle(),
                savedNotification.getMessage(),
                savedNotification.getStatus(),
                savedNotification.getCreatedAt()
        );

            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/notification",
                    message
            );

    }

    @Override
    public NotificationResponse markAsRead(NotificationRequest request) {

        Notification notification =iNotificationRepository.findById(request.id())
                .orElseThrow(()->
                        new EntityNotFoundException("Notification with id " +
                                request.id() + " not found",
                                ErrorCodes.USER_NOT_FOUND));
        notification.setStatus(NotificationStatus.READ);
        return notificationMapper.toResponse(iNotificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponse> findAllByUserId(Integer userId) {
        if(userId==null){
            throw new InvalidOperationException("userId is null");
        }
        User reciver = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User Not Found",ErrorCodes.USER_NOT_FOUND)
        );

        return iNotificationRepository.findByUserId(reciver.getId()).stream()
                .map(notificationMapper::toResponse).toList();
    }



    public record NotificationMessage(
            Integer id,
            String title,
            String message,
            NotificationStatus status,
            LocalDateTime createdAt
    ) {}
}

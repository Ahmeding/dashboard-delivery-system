package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.INotificationRepository;
import com.hitecart.deliverysystem.dto.basic.NotificationDto;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import com.hitecart.deliverysystem.services.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository iNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UtilisateurServiceImpl utilisateurServiceImpl;

    @Override
    public void sendNotification(Integer userId, NotificationDto dto) {
        log.info("sen nottif to {} with payload: {}", userId, dto);
        Notification notification = NotificationDto.toEntity(dto);
        Utilisateur reciver = UtilisateurDto.toEntity(utilisateurServiceImpl.findById(userId));

        notification.setUser(reciver);
        notification.setStatus(NotificationStatus.SEND);

            iNotificationRepository.save(notification);
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/notification",
                    notification
            );
    }

    @Override
    public NotificationDto markAsRead(NotificationDto dto) {

        Notification notification =iNotificationRepository.findById(dto.getId())
                .orElseThrow(()->
                        new EntityNotFoundException("Notification with id " +
                                dto.getId() + " not found",
                                ErrorCodes.UTILISATEUR_NOT_FOUND));
        notification.setStatus(NotificationStatus.READ);
        return NotificationDto.fromEntity(iNotificationRepository.save(notification));
    }

    @Override
    public List<NotificationDto> findAllByUserId(Integer userId) {
        if(userId==null){
            throw new InvalidOperationException("userId is null");
        }
        Utilisateur utilisateur = UtilisateurDto.toEntity(utilisateurServiceImpl.findById(userId));

        return iNotificationRepository.findByUserId(utilisateur.getId()).stream()
                .map(NotificationDto::fromEntity).toList();
    }
}

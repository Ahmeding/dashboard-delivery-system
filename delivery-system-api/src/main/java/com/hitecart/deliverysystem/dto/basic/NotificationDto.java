package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationDto {
    private Integer id;
    private String title;
    private String message;
    private NotificationStatus status;
    private Integer userId;


    public static NotificationDto fromEntity(Notification notification) {
        if(notification == null){
            return null;
        }
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .userId(notification.getUser()!=null?notification.getUser().getId():null)
                .build();
    }

    public static Notification toEntity(NotificationDto dto) {
        if(dto==null){
            return null;
        }
        return Notification.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .status(dto.getStatus())
                .user(Utilisateur.builder().id(dto.getUserId()).build())
                .build();
    }

}

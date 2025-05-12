package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDto {
    private Integer id;
    private String title;
    private String message;
    private NotificationStatus status;
    private Integer userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

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

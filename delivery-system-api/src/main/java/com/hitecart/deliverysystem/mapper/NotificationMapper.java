package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.notification.NotificationRequest;
import com.hitecart.deliverysystem.dto.notification.NotificationResponse;
import com.hitecart.deliverysystem.model.Notification;
import com.hitecart.deliverysystem.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        UserMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface NotificationMapper {

    @Mapping(target = "user", source = "userId")
    Notification toEntity(NotificationRequest request);

    @Mapping(target = "user", source = "user")
    Notification toEntity(NotificationResponse response);


    @Mapping(target = "userId", source = "user.id")
    NotificationRequest toRequest(Notification entity);


    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    NotificationResponse toResponse(Notification entity);

    default User mapUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}

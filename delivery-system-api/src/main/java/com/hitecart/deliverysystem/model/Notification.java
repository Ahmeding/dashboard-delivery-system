package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {
    private String title;
    private String message;
    private NotificationStatus status;
}

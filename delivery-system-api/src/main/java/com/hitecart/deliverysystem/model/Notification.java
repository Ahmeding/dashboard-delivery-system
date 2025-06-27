package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hitecart.deliverysystem.model.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Entity
@Table(name = "notification")
@Builder
public class Notification extends AbstractEntity{

    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @JsonIgnoreProperties("notifications")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

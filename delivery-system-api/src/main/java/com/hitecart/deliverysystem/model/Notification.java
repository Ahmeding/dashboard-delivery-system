package com.hitecart.deliverysystem.model;

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
@Table
public class Notification extends AbstractEntity{

    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Utilisateur user;


}

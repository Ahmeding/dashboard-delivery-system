package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(int customerId);
}

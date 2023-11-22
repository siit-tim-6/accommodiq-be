package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Notification;

import java.util.Collection;

public interface INotificationService {
    Collection<Notification> getAll();

    Notification findNotification(Long notificationId);

    Notification insert(Notification notification);

    Notification update(Notification notification);

    Notification delete(Long notificationId);

    Collection<Notification> findUsersNotifications(Long userId);

    void deleteAll();
}

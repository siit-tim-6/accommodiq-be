package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.Notification;

import java.util.Collection;

public interface INotificationService {
    Collection<Notification> getAll();

    Notification findNotification(Long notificationId);

    Notification insert(Long userId, Notification notification);

    Notification update(Notification notification);

    Notification delete(Long notificationId);

    void deleteAll();

    Collection<Notification> getAllByUserId(Long userId);
}

package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.Notification;

import java.util.Collection;

public interface INotificationService {
    Notification findNotification(Long notificationId);

    Notification delete(Long notificationId);

    Collection<Notification> getAllByUserId(Long userId);

    void createAndSendNotification(Notification notification);
}

package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.dtos.NotificationDto;

import java.util.Collection;

public interface INotificationService {
    Notification findNotification(Long notificationId);

    Notification delete(Long notificationId);

    Collection<NotificationDto> getAllByUserId(Long userId);

    void createAndSendNotification(Notification notification);

    void markAllAsSeen(Long userId);

    void markAsSeen(Long userId, Long notificationId);
}

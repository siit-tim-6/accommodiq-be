package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.NotificationSetting;

import java.util.Collection;

public interface INotificationSettingService {
    Collection<NotificationSetting> getAll();

    NotificationSetting findNotificationSetting(Long notificationSettingId);

    NotificationSetting insert(Long userId, NotificationSetting review);

    NotificationSetting update(NotificationSetting review);

    NotificationSetting delete(Long reviewId);

    void deleteAll();

    void setNotificationSettingsForUser(Long userId);
}
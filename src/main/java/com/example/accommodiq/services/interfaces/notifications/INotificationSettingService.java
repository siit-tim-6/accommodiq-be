package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.dtos.NotificationSettingDto;

import java.util.Collection;
import java.util.List;

public interface INotificationSettingService {
    Collection<NotificationSetting> getAll();

    NotificationSetting findNotificationSetting(Long notificationSettingId);

    NotificationSetting insert(Long userId, NotificationSetting review);

    NotificationSetting update(NotificationSetting review);

    NotificationSetting delete(Long reviewId);

    void deleteAll();

    void setNotificationSettingsForUser(Long userId);

    List<NotificationSetting> getAllByUserId(Long userId);

    List<NotificationSettingDto> update(Long userId, List<NotificationSettingDto> notificationSettings);
}

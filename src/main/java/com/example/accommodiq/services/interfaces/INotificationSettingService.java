package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.repositories.NotificationSettingRepository;

import java.util.Collection;

public interface INotificationSettingService {
    Collection<NotificationSettingDto> findUsersNotificationSettings(Long userId);
    NotificationSetting insert(NotificationSetting notificationSetting);
    void setNotificationSettingsForUser(User user);

    Collection<NotificationSettingDto> updateNotificationSettingsForUser(User user, Collection<NotificationSetting> notificationSettings);
}

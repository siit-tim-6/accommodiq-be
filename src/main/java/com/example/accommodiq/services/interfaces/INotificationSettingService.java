package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.repositories.NotificationSettingRepository;

import java.util.Collection;

public interface INotificationSettingService {
    Collection<NotificationSetting> findUsersNotificationSettings(Long userId);
    NotificationSetting insert(NotificationSetting notificationSetting);

}

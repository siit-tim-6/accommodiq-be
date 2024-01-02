package com.example.accommodiq.services.interfaces.notifications;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.enums.AccountRole;

import java.util.List;

public interface INotificationSettingService {
    void setNotificationSettingsForUser(User user, AccountRole role);

    List<NotificationSetting> getUserNotificationSettings(Long userId);

    List<NotificationSettingDto> update(Long userId, List<NotificationSettingDto> notificationSettings);
}

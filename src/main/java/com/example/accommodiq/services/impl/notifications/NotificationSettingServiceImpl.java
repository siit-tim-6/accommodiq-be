package com.example.accommodiq.services.impl.notifications;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.repositories.NotificationSettingRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSettingServiceImpl implements INotificationSettingService {

    final NotificationSettingRepository allNotificationSettings;

    @Autowired
    public NotificationSettingServiceImpl(NotificationSettingRepository allNotificationSettings) {
        this.allNotificationSettings = allNotificationSettings;
    }

    @Override
    public void setNotificationSettingsForUser(User user, AccountRole role) {
        if (role == AccountRole.ADMIN) {
            return;
        }
        if (role == AccountRole.GUEST) {
            NotificationSetting setting = new NotificationSetting(null, NotificationType.HOST_REPLY_TO_REQUEST, true, user);
            allNotificationSettings.save(setting);
        }
        if (role == AccountRole.HOST) {
            for (NotificationType type : NotificationType.values()) {
                if (type == NotificationType.HOST_REPLY_TO_REQUEST) continue;
                NotificationSetting setting = new NotificationSetting(null, type, true, user);
                allNotificationSettings.save(setting);
            }
        }
    }

    @Override
    public List<NotificationSetting> getUserNotificationSettings(Long userId) {
        return allNotificationSettings.findAllByUserId(userId);
    }

    @Override
    public List<NotificationSettingDto> update(Long userId, List<NotificationSettingDto> notificationSettingsToUpdate) {
        List<NotificationSetting> userNotificationSettings = getUserNotificationSettings(userId);
        for (NotificationSettingDto settingToUpdate : notificationSettingsToUpdate) {
            NotificationSetting setting = findNotificationSettingByType(userNotificationSettings, settingToUpdate.getType());

            if (setting == null) continue;

            setting.setOn(settingToUpdate.isOn());
            allNotificationSettings.save(setting);
        }
        return userNotificationSettings.stream().map(NotificationSettingDto::new).toList();
    }

    private NotificationSetting findNotificationSettingByType(List<NotificationSetting> notificationSettings, NotificationType type) {
        for (NotificationSetting setting : notificationSettings) {
            if (setting.getType().equals(type)) {
                return setting;
            }
        }
        return null;
    }
}

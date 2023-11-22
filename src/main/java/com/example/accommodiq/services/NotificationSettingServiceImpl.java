package com.example.accommodiq.services;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.repositories.NotificationSettingRepository;
import com.example.accommodiq.services.interfaces.INotificationSettingService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class NotificationSettingServiceImpl implements INotificationSettingService {

    final
    NotificationSettingRepository allNotificationSettings;

    @Autowired
    public NotificationSettingServiceImpl(NotificationSettingRepository allNotificationSettings) {
        this.allNotificationSettings = allNotificationSettings;
    }

    @Override
    public Collection<NotificationSetting> findUsersNotificationSettings(Long userId) {
        return allNotificationSettings.findNotificationSettingsByUserId(userId);
    }

    @Override
    public NotificationSetting insert(NotificationSetting notificationSetting) {
        try {
            allNotificationSettings.save(notificationSetting);
            allNotificationSettings.flush();
            return notificationSetting;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Notification setting cannot be inserted");
        }
    }

    @Override
    @Transactional
    public void setNotificationSettingsForUser(User user) {
        for (NotificationSetting.NotificationType type : NotificationSetting.NotificationType.values()) {
           insert(new NotificationSetting((long) -1, user, type, true));
        }
    }
}

package com.example.accommodiq.services;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.repositories.NotificationSettingRepository;
import com.example.accommodiq.services.interfaces.INotificationSettingService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class NotificationSettingServiceImpl implements INotificationSettingService {

    final
    NotificationSettingRepository allNotificationSettings;

    @Autowired
    public NotificationSettingServiceImpl(NotificationSettingRepository allNotificationSettings) {
        this.allNotificationSettings = allNotificationSettings;
    }

    @Override
    public Collection<NotificationSettingDto> findUsersNotificationSettings(Long userId) {
        return allNotificationSettings.findNotificationSettingsByUserId(userId).stream().map(NotificationSettingDto::new).toList();
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

    @Override
    @Transactional
    public Collection<NotificationSettingDto> updateNotificationSettingsForUser(User user, Collection<NotificationSetting> notificationSettings) {
        Collection<NotificationSettingDto> updatedSettingDtos = new ArrayList<>();

        for (NotificationSetting notificationSetting : notificationSettings) {
                NotificationSetting existingNotification = allNotificationSettings.findNotificationSettingByUserIdAndType(user.getId(), notificationSetting.getType());
                existingNotification.setOn(notificationSetting.isOn());
                allNotificationSettings.save(existingNotification);
                updatedSettingDtos.add(new NotificationSettingDto(existingNotification));
        }
        return updatedSettingDtos;
    }
}

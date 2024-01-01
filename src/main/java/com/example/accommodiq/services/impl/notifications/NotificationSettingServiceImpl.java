package com.example.accommodiq.services.impl.notifications;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.repositories.NotificationSettingRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationSettingService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.accommodiq.utilities.ErrorUtils.generateNotFound;

@Service
public class NotificationSettingServiceImpl implements INotificationSettingService {

    final NotificationSettingRepository allNotificationSettings;

    final IUserService userService;

    @Autowired
    public NotificationSettingServiceImpl(NotificationSettingRepository allNotificationSettings, IUserService userService) {
        this.allNotificationSettings = allNotificationSettings;
        this.userService = userService;
    }


    @Override
    public Collection<NotificationSetting> getAll() {
        return allNotificationSettings.findAll();
    }

    @Override
    public NotificationSetting findNotificationSetting(Long notificationSettingId) {
        Optional<NotificationSetting> found = allNotificationSettings.findById(notificationSettingId);
        if (found.isEmpty()) {
            throw generateNotFound("reviewNotFound");
        }
        return found.get();
    }

    @Override
    public NotificationSetting insert(Long userId, NotificationSetting notificationSetting) {
        User user = userService.findUser(userId);
        //user.getNotificationSettings().add(notificationSetting);
        userService.update(user);
        return notificationSetting;
    }

    @Override
    public NotificationSetting update(NotificationSetting notificationSetting) {
        try {
            findNotificationSetting(notificationSetting.getId());
            allNotificationSettings.save(notificationSetting);
            allNotificationSettings.flush();
            return notificationSetting;
        } catch (ConstraintViolationException ex) {
            throw generateNotFound("reviewUpdateFailed");
        }
    }

    @Override
    public NotificationSetting delete(Long reviewId) {
        NotificationSetting notificationSetting = findNotificationSetting(reviewId);
        allNotificationSettings.delete(notificationSetting);
        allNotificationSettings.flush();
        return notificationSetting;
    }

    @Override
    public void deleteAll() {
        allNotificationSettings.deleteAll();
        allNotificationSettings.flush();
    }

    @Override
    public void setNotificationSettingsForUser(Long userId) {
        User user = userService.findUser(userId);
        for (NotificationType type : NotificationType.values()) {
            // user.getNotificationSettings().add(new NotificationSetting((long) -1, type, true));
        }
        userService.update(user);
    }

    @Override
    public List<NotificationSetting> getAllByUserId(Long userId) {
        return allNotificationSettings.findAllByUserId(userId);
    }
}

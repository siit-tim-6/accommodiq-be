package com.example.accommodiq.services.impl.notifications;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.repositories.NotificationRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
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
public class NotificationServiceImpl implements INotificationService {

    final
    NotificationRepository allNotifications;

    final IUserService userService;

    final INotificationSettingService notificationSettingService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository allNotifications, IUserService userService, INotificationSettingService notificationSettingService) {
        this.allNotifications = allNotifications;
        this.userService = userService;
        this.notificationSettingService = notificationSettingService;
    }

    @Override
    public Collection<Notification> getAll() {
        return allNotifications.findAll();
    }

    @Override
    public Notification findNotification(Long notificationId) {
        Optional<Notification> found = allNotifications.findById(notificationId);
        if (found.isEmpty()) {
            throw generateNotFound("reviewNotFound");
        }
        return found.get();
    }

    @Override
    public Notification insert(Long userId, Notification notification) {
        User user = userService.findUser(userId);
//        user.getNotifications().add(notification);
        userService.update(user);
        return notification;
    }

    @Override
    public Notification update(Notification notification) {
        try {
            findNotification(notification.getId());
            allNotifications.save(notification);
            allNotifications.flush();
            return notification;
        } catch (ConstraintViolationException ex) {
            throw generateNotFound("reviewUpdateFailed");
        }
    }

    @Override
    public Notification delete(Long notificationId) {
        Notification notification = findNotification(notificationId);
        allNotifications.delete(notification);
        allNotifications.flush();
        return notification;
    }

    @Override
    public void deleteAll() {
        allNotifications.deleteAll();
        allNotifications.flush();
    }

    @Override
    public Collection<Notification> getAllByUserId(Long userId) {
        List<NotificationType> notificationTypes = notificationSettingService.getUserNotificationSettings(userId).stream()
                .filter(NotificationSetting::isOn).map(NotificationSetting::getType).toList();
        return allNotifications.findAllByUserIdAndTypeIsInOrderByTimeDesc(userId, notificationTypes);
    }
}

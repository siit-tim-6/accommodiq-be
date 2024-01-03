package com.example.accommodiq.services.impl.notifications;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.repositories.NotificationRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.notifications.INotificationSettingService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(NotificationRepository allNotifications, IUserService userService, INotificationSettingService notificationSettingService, SimpMessagingTemplate messagingTemplate) {
        this.allNotifications = allNotifications;
        this.userService = userService;
        this.notificationSettingService = notificationSettingService;
        this.messagingTemplate = messagingTemplate;
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
    public Notification delete(Long notificationId) {
        Notification notification = findNotification(notificationId);
        allNotifications.delete(notification);
        allNotifications.flush();
        return notification;
    }

    @Override
    public Collection<Notification> getAllByUserId(Long userId) {
        List<NotificationType> notificationTypes = notificationSettingService.getUserNotificationSettings(userId).stream()
                .filter(NotificationSetting::isOn).map(NotificationSetting::getType).toList();
        return allNotifications.findAllByUserIdAndTypeIsInOrderByTimeDesc(userId, notificationTypes);
    }

    @Override
    public void createAndSendNotification(Notification notification) {
        allNotifications.save(notification);
        allNotifications.flush();
        long id = notification.getUser().getId();
        notification.setUser(null);
        messagingTemplate.convertAndSend("/socket-publisher/" + id, notification);
    }

    @Override
    public void markAllAsSeen(Long userId) {
        allNotifications.findAllByUserIdAndSeenIsFalse(userId).forEach(notification -> {
            notification.setSeen(true);
            allNotifications.save(notification);
        });
        allNotifications.flush();
    }
}

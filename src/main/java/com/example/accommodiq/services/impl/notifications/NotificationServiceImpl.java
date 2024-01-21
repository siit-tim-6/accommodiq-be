package com.example.accommodiq.services.impl.notifications;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.dtos.NotificationDto;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.enums.NotificationType;
import com.example.accommodiq.repositories.NotificationRepository;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.notifications.INotificationSettingService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    final FirebaseMessaging firebaseMessaging;

    @Autowired
    public NotificationServiceImpl(NotificationRepository allNotifications, IUserService userService, INotificationSettingService notificationSettingService, SimpMessagingTemplate messagingTemplate, FirebaseMessaging firebaseMessaging) {
        this.allNotifications = allNotifications;
        this.userService = userService;
        this.notificationSettingService = notificationSettingService;
        this.messagingTemplate = messagingTemplate;
        this.firebaseMessaging = firebaseMessaging;
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
    public Collection<NotificationDto> getAllByUserId(Long userId) {
        List<NotificationType> notificationTypes = notificationSettingService.getUserNotificationSettings(userId).stream()
                .filter(NotificationSettingDto::isOn).map(NotificationSettingDto::getType).toList();
        return allNotifications.findAllByUserIdAndTypeIsInOrderByTimeDesc(userId, notificationTypes).stream().map(NotificationDto::new).toList();
    }

    @Override
    public void createAndSendNotification(Notification notification) {
        allNotifications.save(notification);
        allNotifications.flush();
        NotificationDto notificationDto = new NotificationDto(notification);
        messagingTemplate.convertAndSend("/socket-publisher/" + notification.getUser().getId(), notificationDto);

        Message message = Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(notification.getType().toString())
                        .setBody(notification.getText())
                        .build())
                .setTopic("user-" + notification.getUser().getId())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(502), "Firebase error");
        }
    }

    @Override
    public void markAllAsSeen(Long userId) {
        allNotifications.findAllByUserIdAndSeenIsFalse(userId).forEach(notification -> {
            notification.setSeen(true);
            allNotifications.save(notification);
        });
        allNotifications.flush();
    }

    @Override
    public void markAsSeen(Long userId, Long notificationId) {
        Notification notification = findNotification(notificationId);
        if (!notification.getUser().getId().equals(userId)) {
            throw generateNotFound("notificationNotFound");
        }
        notification.setSeen(true);
        allNotifications.save(notification);
        allNotifications.flush();
    }

    @Override
    @Transactional
    public void deleteByUserId(Long id) {
        allNotifications.deleteByUserId(id);
    }
}

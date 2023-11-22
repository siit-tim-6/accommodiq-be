package com.example.accommodiq.services;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.repositories.NotificationRepository;
import com.example.accommodiq.services.interfaces.INotificationService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class NotificationServiceImpl implements INotificationService {
    
    final
    NotificationRepository allNotifications;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public NotificationServiceImpl(NotificationRepository allNotifications) {
        this.allNotifications = allNotifications;
    }

    @Override
    public Collection<Notification> getAll() {
        return allNotifications.findAll();
    }

    @Override
    public Notification findNotification(Long notificationId) {
        Optional<Notification> found = allNotifications.findById(notificationId);
        if (found.isEmpty()) {
            String value = bundle.getString("notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Notification insert(Notification notification) {
        try {
            allNotifications.save(notification);
            allNotifications.flush();
            return notification;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Notification cannot be inserted");
        }
    }

    @Override
    public Notification update(Notification notification) {
        try {
            findNotification(notification.getId()); // this will throw ResponseStatusException if notification is not found
            allNotifications.save(notification);
            allNotifications.flush();
            return notification;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = c;
            }
            if ((c != null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Notification cannot be updated");
            }
            throw ex;
        }
    }

    @Override
    public Notification delete(Long notificationId) {
        Notification found = findNotification(notificationId); // this will throw NotificationNotFoundException if Notification is not found
        allNotifications.delete(found);
        allNotifications.flush();
        return found;
    }

    @Override
    public void deleteAll() {
        allNotifications.deleteAll();
        allNotifications.flush();
    }

    @Override
    public Collection<Notification> findUsersNotifications(Long userId) {
        return allNotifications.findByUserId(userId);
    }
}

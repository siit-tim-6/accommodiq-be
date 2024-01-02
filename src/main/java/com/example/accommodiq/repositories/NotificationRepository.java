package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserIdAndTypeIsInOrderByTimeDesc(Long userId, List<NotificationType> types);
}

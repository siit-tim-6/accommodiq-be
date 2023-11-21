package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Collection<Notification> findByUserId(Long userId);
}

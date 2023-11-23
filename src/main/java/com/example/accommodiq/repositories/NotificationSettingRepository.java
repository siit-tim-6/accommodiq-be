package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    Collection<NotificationSetting> findNotificationSettingsByUserId(Long userId);
    NotificationSetting findNotificationSettingByUserIdAndType(Long userId, NotificationType type);
}

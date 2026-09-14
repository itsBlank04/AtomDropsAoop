package com.atomdrops.notification.repository;
import com.atomdrops.notification.model.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {
    List<SystemNotification> findByIsActiveTrueAndStartsAtBeforeOrderByStartsAtDesc(java.time.Instant now);
}

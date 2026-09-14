package com.atomdrops.notification.repository;
import com.atomdrops.notification.model.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {
    List<NotificationSubscription> findByUserId(Long userId);
}

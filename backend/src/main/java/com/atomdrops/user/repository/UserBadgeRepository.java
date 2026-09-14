package com.atomdrops.user.repository;
import com.atomdrops.user.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUserIdAndIsActiveTrue(Long userId);
}

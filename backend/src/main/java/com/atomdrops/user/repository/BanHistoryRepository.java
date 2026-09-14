package com.atomdrops.user.repository;
import com.atomdrops.user.model.BanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BanHistoryRepository extends JpaRepository<BanHistory, Long> {
    List<BanHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}

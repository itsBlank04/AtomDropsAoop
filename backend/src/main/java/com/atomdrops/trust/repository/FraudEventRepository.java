package com.atomdrops.trust.repository;
import com.atomdrops.trust.model.FraudEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FraudEventRepository extends JpaRepository<FraudEvent, Long> {
    List<FraudEvent> findByUserId(Long userId);
    List<FraudEvent> findBySeverity(String severity);
}

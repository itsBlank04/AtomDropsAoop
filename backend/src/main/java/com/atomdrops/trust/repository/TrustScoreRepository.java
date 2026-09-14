package com.atomdrops.trust.repository;
import com.atomdrops.trust.model.TrustScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TrustScoreRepository extends JpaRepository<TrustScore, Long> {
    Optional<TrustScore> findByUserId(Long userId);
}

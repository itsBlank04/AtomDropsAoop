package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.BidderReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BidderReputationRepository extends JpaRepository<BidderReputation, Long> {
    Optional<BidderReputation> findByUserId(Long userId);
}

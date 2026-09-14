package com.atomdrops.marketplace.repository;
import com.atomdrops.marketplace.model.UsedItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsedItemHistoryRepository extends JpaRepository<UsedItemHistory, Long> {
    Optional<UsedItemHistory> findByListingId(Long listingId);
}

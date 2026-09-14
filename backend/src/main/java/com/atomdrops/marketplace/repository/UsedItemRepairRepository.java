package com.atomdrops.marketplace.repository;
import com.atomdrops.marketplace.model.UsedItemRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UsedItemRepairRepository extends JpaRepository<UsedItemRepair, Long> {
    List<UsedItemRepair> findByListingId(Long listingId);
}

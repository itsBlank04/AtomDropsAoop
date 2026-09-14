package com.atomdrops.marketplace.repository;
import com.atomdrops.marketplace.model.UsedListingOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface UsedListingOfferRepository extends JpaRepository<UsedListingOffer, Long> {
    List<UsedListingOffer> findByListingId(Long listingId);
    List<UsedListingOffer> findByBuyerId(Long buyerId);
    Optional<UsedListingOffer> findByListingIdAndBuyerId(Long listingId, Long buyerId);
}

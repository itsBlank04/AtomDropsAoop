package com.atomdrops.marketplace.repository;
import com.atomdrops.marketplace.model.UsedListingOfferMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UsedListingOfferMessageRepository extends JpaRepository<UsedListingOfferMessage, Long> {
    List<UsedListingOfferMessage> findByOfferIdOrderByCreatedAtAsc(Long offerId);
}

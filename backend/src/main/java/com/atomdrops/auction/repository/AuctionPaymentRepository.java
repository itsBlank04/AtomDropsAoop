package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.AuctionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AuctionPaymentRepository extends JpaRepository<AuctionPayment, Long> {
    Optional<AuctionPayment> findByAuctionWinnerId(Long auctionWinnerId);
}

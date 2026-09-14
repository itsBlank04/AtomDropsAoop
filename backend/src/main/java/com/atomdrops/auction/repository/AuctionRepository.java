package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByVendorId(Long vendorId);
    List<Auction> findByShopId(Long shopId);
    List<Auction> findByStatus(String status);
    List<Auction> findByStatusAndType(String status, String type);
    List<Auction> findByStatusAndStartTimeBefore(String status, Instant time);
    List<Auction> findByStatusAndEndTimeBefore(String status, Instant time);
}

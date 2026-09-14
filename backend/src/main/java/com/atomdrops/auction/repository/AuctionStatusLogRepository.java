package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.AuctionStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuctionStatusLogRepository extends JpaRepository<AuctionStatusLog, Long> {
    List<AuctionStatusLog> findByAuctionIdOrderByChangedAtDesc(Long auctionId);
}

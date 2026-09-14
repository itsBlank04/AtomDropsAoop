package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.AuctionApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuctionApprovalRepository extends JpaRepository<AuctionApproval, Long> {
    List<AuctionApproval> findByAuctionId(Long auctionId);
}

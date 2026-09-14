package com.atomdrops.auction.repository;

import com.atomdrops.auction.model.AuctionBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionBookmarkRepository extends JpaRepository<AuctionBookmark, Long> {
    List<AuctionBookmark> findByUserId(Long userId);
    Optional<AuctionBookmark> findByUserIdAndAuctionId(Long userId, Long auctionId);
    boolean existsByUserIdAndAuctionId(Long userId, Long auctionId);
    long countByAuctionId(Long auctionId);
    void deleteByUserIdAndAuctionId(Long userId, Long auctionId);
}

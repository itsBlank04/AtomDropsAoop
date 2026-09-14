package com.atomdrops.auction.repository;
import com.atomdrops.auction.model.AuctionWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AuctionWatchlistRepository extends JpaRepository<AuctionWatchlist, Long> {
    List<AuctionWatchlist> findByUserId(Long userId);
    Optional<AuctionWatchlist> findByUserIdAndLotId(Long userId, Long lotId);
    boolean existsByUserIdAndLotId(Long userId, Long lotId);
    long countByLotId(Long lotId);
    void deleteByUserIdAndLotId(Long userId, Long lotId);
}

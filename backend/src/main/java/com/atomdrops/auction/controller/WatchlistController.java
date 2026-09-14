package com.atomdrops.auction.controller;

import com.atomdrops.security.SecurityConfig;
import com.atomdrops.auction.model.AuctionLot;
import com.atomdrops.auction.model.AuctionWatchlist;
import com.atomdrops.user.model.User;
import com.atomdrops.auction.repository.AuctionLotRepository;
import com.atomdrops.auction.repository.AuctionWatchlistRepository;
import com.atomdrops.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final AuctionWatchlistRepository auctionWatchlistRepository;
    private final AuctionLotRepository auctionLotRepository;
    private final UserRepository userRepository;

    public WatchlistController(AuctionWatchlistRepository auctionWatchlistRepository,
            AuctionLotRepository auctionLotRepository, UserRepository userRepository) {
        this.auctionWatchlistRepository = auctionWatchlistRepository;
        this.auctionLotRepository = auctionLotRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<AuctionWatchlist> list(HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        return auctionWatchlistRepository.findByUserId(userId);
    }

    @GetMapping("/{lotId}")
    public Map<String, Object> status(@PathVariable Long lotId, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        return Map.of(
                "lotId", lotId,
                "watched", auctionWatchlistRepository.existsByUserIdAndLotId(userId, lotId),
                "watchers", auctionWatchlistRepository.countByLotId(lotId)
        );
    }

    @PostMapping("/{lotId}")
    public AuctionWatchlist add(@PathVariable Long lotId, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        var existing = auctionWatchlistRepository.findByUserIdAndLotId(userId, lotId);
        if (existing.isPresent()) return existing.get();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        AuctionLot lot = auctionLotRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Lot not found"));
        AuctionWatchlist entry = new AuctionWatchlist();
        entry.setUser(user);
        entry.setLot(lot);
        return auctionWatchlistRepository.save(entry);
    }

    @DeleteMapping("/{lotId}")
    @Transactional
    public Map<String, String> remove(@PathVariable Long lotId, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        auctionWatchlistRepository.deleteByUserIdAndLotId(userId, lotId);
        return Map.of("message", "Removed");
    }
}

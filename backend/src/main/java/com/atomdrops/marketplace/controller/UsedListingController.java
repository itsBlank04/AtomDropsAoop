package com.atomdrops.marketplace.controller;

import com.atomdrops.product.repository.CategoryRepository;
import com.atomdrops.product.model.ConditionLevel;
import com.atomdrops.marketplace.model.UsedImage;
import com.atomdrops.marketplace.model.UsedItemHistory;
import com.atomdrops.marketplace.model.UsedItemRepair;
import com.atomdrops.marketplace.model.UsedListing;
import com.atomdrops.marketplace.model.UsedVideo;
import com.atomdrops.user.model.User;
import com.atomdrops.product.repository.ConditionLevelRepository;
import com.atomdrops.marketplace.repository.UsedImageRepository;
import com.atomdrops.marketplace.repository.UsedItemHistoryRepository;
import com.atomdrops.marketplace.repository.UsedItemRepairRepository;
import com.atomdrops.marketplace.repository.UsedListingRepository;
import com.atomdrops.marketplace.repository.UsedVideoRepository;
import com.atomdrops.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/used-listings")
public class UsedListingController {

    private final UsedListingRepository usedListingRepository;
    private final UsedImageRepository usedImageRepository;
    private final UsedVideoRepository usedVideoRepository;
    private final UsedItemHistoryRepository usedItemHistoryRepository;
    private final UsedItemRepairRepository usedItemRepairRepository;
    private final ConditionLevelRepository conditionLevelRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public UsedListingController(UsedListingRepository usedListingRepository,
            UsedImageRepository usedImageRepository, UsedVideoRepository usedVideoRepository,
            UsedItemHistoryRepository usedItemHistoryRepository,
            UsedItemRepairRepository usedItemRepairRepository,
            ConditionLevelRepository conditionLevelRepository,
            CategoryRepository categoryRepository, UserRepository userRepository) {
        this.usedListingRepository = usedListingRepository;
        this.usedImageRepository = usedImageRepository;
        this.usedVideoRepository = usedVideoRepository;
        this.usedItemHistoryRepository = usedItemHistoryRepository;
        this.usedItemRepairRepository = usedItemRepairRepository;
        this.conditionLevelRepository = conditionLevelRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UsedListing> list(@RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long condition,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<UsedListing> listings = usedListingRepository.findByStatusAndDeletedAtIsNull("ACTIVE");
        if (category != null) {
            listings = listings.stream().filter(l -> l.getCategory() != null && l.getCategory().getId().equals(category)).collect(Collectors.toList());
        }
        if (condition != null) {
            listings = listings.stream().filter(l -> l.getCondition() != null && l.getCondition().getId().equals(condition)).collect(Collectors.toList());
        }
        if (minPrice != null) {
            listings = listings.stream().filter(l -> l.getPriceBdt().compareTo(minPrice) >= 0).collect(Collectors.toList());
        }
        if (maxPrice != null) {
            listings = listings.stream().filter(l -> l.getPriceBdt().compareTo(maxPrice) <= 0).collect(Collectors.toList());
        }
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            listings = listings.stream().filter(l -> l.getTitle().toLowerCase().contains(q) || (l.getDescription() != null && l.getDescription().toLowerCase().contains(q))).collect(Collectors.toList());
        }
        return listings;
    }

    @GetMapping("/{id}")
    public UsedListing getOne(@PathVariable Long id) {
        return usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
    }

    @PostMapping
    public UsedListing create(@RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        User seller = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UsedListing listing = new UsedListing();
        listing.setSeller(seller);
        listing.setTitle((String) body.get("title"));
        listing.setDescription((String) body.get("description"));
        listing.setPriceBdt(new BigDecimal(body.get("priceBdt").toString()));
        if (body.containsKey("warrantyFlag")) listing.setWarrantyFlag((String) body.get("warrantyFlag"));
        if (body.containsKey("offersEnabled")) listing.setOffersEnabled(Boolean.parseBoolean(body.get("offersEnabled").toString()));
        if (body.containsKey("categoryId")) {
            listing.setCategory(categoryRepository.findById(Long.valueOf(body.get("categoryId").toString())).orElse(null));
        }
        if (body.containsKey("conditionId")) {
            listing.setCondition(conditionLevelRepository.findById(Long.valueOf(body.get("conditionId").toString())).orElse(null));
        }
        return usedListingRepository.save(listing);
    }

    @PutMapping("/{id}")
    public UsedListing update(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        if (body.containsKey("title")) listing.setTitle((String) body.get("title"));
        if (body.containsKey("description")) listing.setDescription((String) body.get("description"));
        if (body.containsKey("priceBdt")) listing.setPriceBdt(new BigDecimal(body.get("priceBdt").toString()));
        if (body.containsKey("warrantyFlag")) listing.setWarrantyFlag((String) body.get("warrantyFlag"));
        if (body.containsKey("offersEnabled")) listing.setOffersEnabled(Boolean.parseBoolean(body.get("offersEnabled").toString()));
        if (body.containsKey("categoryId")) {
            listing.setCategory(categoryRepository.findById(Long.valueOf(body.get("categoryId").toString())).orElse(null));
        }
        if (body.containsKey("conditionId")) {
            listing.setCondition(conditionLevelRepository.findById(Long.valueOf(body.get("conditionId").toString())).orElse(null));
        }
        return usedListingRepository.save(listing);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        listing.setDeletedAt(Instant.now());
        usedListingRepository.save(listing);
        return Map.of("message", "Deleted");
    }

    @PostMapping("/{id}/images")
    public UsedImage addImage(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        UsedImage image = new UsedImage();
        image.setListing(listing);
        image.setImageUrl((String) body.get("imageUrl"));
        if (body.containsKey("sortOrder")) image.setSortOrder(Integer.parseInt(body.get("sortOrder").toString()));
        return usedImageRepository.save(image);
    }

    @PostMapping("/{id}/videos")
    public UsedVideo addVideo(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        UsedVideo video = new UsedVideo();
        video.setListing(listing);
        video.setVideoUrl((String) body.get("videoUrl"));
        return usedVideoRepository.save(video);
    }

    @PutMapping("/{id}/history")
    public UsedItemHistory updateHistory(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        UsedItemHistory history = usedItemHistoryRepository.findByListingId(id).orElseGet(() -> {
            UsedItemHistory h = new UsedItemHistory();
            h.setListing(listing);
            return h;
        });
        if (body.containsKey("ownerCount")) history.setOwnerCount(Integer.parseInt(body.get("ownerCount").toString()));
        if (body.containsKey("usageDurationMonths")) history.setUsageDurationMonths(Integer.parseInt(body.get("usageDurationMonths").toString()));
        return usedItemHistoryRepository.save(history);
    }

    @PostMapping("/{id}/repairs")
    public UsedItemRepair addRepair(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        UsedListing listing = usedListingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        if (!listing.getSeller().getId().equals(userId)) throw new IllegalArgumentException("Not your listing");
        UsedItemRepair repair = new UsedItemRepair();
        repair.setListing(listing);
        repair.setDetails((String) body.get("details"));
        return usedItemRepairRepository.save(repair);
    }

    @GetMapping("/conditions")
    public List<ConditionLevel> getConditions() {
        return conditionLevelRepository.findAll();
    }

    @GetMapping("/mine")
    public List<UsedListing> getMine(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        return usedListingRepository.findBySellerIdAndDeletedAtIsNull(userId);
    }
}

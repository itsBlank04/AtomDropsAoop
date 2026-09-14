package com.atomdrops.marketplace.repository;
import com.atomdrops.marketplace.model.UsedVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UsedVideoRepository extends JpaRepository<UsedVideo, Long> {
    List<UsedVideo> findByListingId(Long listingId);
}

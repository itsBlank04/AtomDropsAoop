package com.atomdrops.shop.repository;
import com.atomdrops.shop.model.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {
    Optional<VendorProfile> findByUserId(Long userId);
    Optional<VendorProfile> findByShopSlug(String shopSlug);
}

package com.atomdrops.shop.repository;

import com.atomdrops.shop.model.VendorSubscriptionDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VendorSubscriptionDealRepository extends JpaRepository<VendorSubscriptionDeal, Long> {
    List<VendorSubscriptionDeal> findByIsActiveTrueAndEndsAtAfter(Instant now);
}

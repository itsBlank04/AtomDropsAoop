package com.atomdrops.shop.repository;

import com.atomdrops.shop.model.VendorSubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorSubscriptionPlanRepository extends JpaRepository<VendorSubscriptionPlan, Long> {
    Optional<VendorSubscriptionPlan> findByName(String name);
}

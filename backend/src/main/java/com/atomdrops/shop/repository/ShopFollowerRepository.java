package com.atomdrops.shop.repository;

import com.atomdrops.shop.model.ShopFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopFollowerRepository extends JpaRepository<ShopFollower, Long> {
    List<ShopFollower> findByShopId(Long shopId);
    List<ShopFollower> findByUserId(Long userId);
    Optional<ShopFollower> findByShopIdAndUserId(Long shopId, Long userId);
    long countByShopId(Long shopId);
    boolean existsByShopIdAndUserId(Long shopId, Long userId);
}

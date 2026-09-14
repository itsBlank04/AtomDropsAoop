package com.atomdrops.product.repository;
import com.atomdrops.product.model.CouponUse;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CouponUseRepository extends JpaRepository<CouponUse, Long> {
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}

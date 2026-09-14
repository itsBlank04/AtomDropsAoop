package com.atomdrops.user.repository;

import com.atomdrops.user.model.UpgradePayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpgradePaymentRepository extends JpaRepository<UpgradePayment, Long> {
    List<UpgradePayment> findByUpgradeIdOrderByCreatedAtDesc(Long upgradeId);
}

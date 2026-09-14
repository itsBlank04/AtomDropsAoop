package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.RepairPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RepairPaymentRepository extends JpaRepository<RepairPayment, Long> {
    Optional<RepairPayment> findByBookingId(Long bookingId);
}

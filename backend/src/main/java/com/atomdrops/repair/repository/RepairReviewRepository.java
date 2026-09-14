package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.RepairReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface RepairReviewRepository extends JpaRepository<RepairReview, Long> {
    Optional<RepairReview> findByBookingId(Long bookingId);
    List<RepairReview> findByTechnicianId(Long technicianId);
    List<RepairReview> findByCustomerId(Long customerId);
}

package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.TechnicianEarning;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface TechnicianEarningRepository extends JpaRepository<TechnicianEarning, Long> {
    List<TechnicianEarning> findByTechnicianId(Long technicianId);
    List<TechnicianEarning> findByStatus(String status);
    Optional<TechnicianEarning> findByBookingId(Long bookingId);
}

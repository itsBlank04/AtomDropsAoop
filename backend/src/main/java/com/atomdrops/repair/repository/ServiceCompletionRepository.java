package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.ServiceCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ServiceCompletionRepository extends JpaRepository<ServiceCompletion, Long> {
    Optional<ServiceCompletion> findByBookingId(Long bookingId);
}

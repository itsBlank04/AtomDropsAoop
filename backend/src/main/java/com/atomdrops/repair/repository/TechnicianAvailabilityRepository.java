package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.TechnicianAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TechnicianAvailabilityRepository extends JpaRepository<TechnicianAvailability, Long> {
    List<TechnicianAvailability> findByTechnicianId(Long technicianId);
}

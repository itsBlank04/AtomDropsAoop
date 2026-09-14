package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.ServiceListing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {
    List<ServiceListing> findByTechnicianId(Long technicianId);
    List<ServiceListing> findByCategoryId(Long categoryId);
    List<ServiceListing> findByStatus(String status);
}

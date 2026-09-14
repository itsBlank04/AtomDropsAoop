package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.TechnicianLevelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TechnicianLevelHistoryRepository extends JpaRepository<TechnicianLevelHistory, Long> {
    List<TechnicianLevelHistory> findByTechnicianIdOrderByChangedAtDesc(Long technicianId);
}

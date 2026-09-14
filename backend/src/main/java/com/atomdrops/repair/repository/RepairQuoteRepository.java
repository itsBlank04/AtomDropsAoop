package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.RepairQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RepairQuoteRepository extends JpaRepository<RepairQuote, Long> {
    List<RepairQuote> findByRequestId(Long requestId);
    List<RepairQuote> findByTechnicianId(Long technicianId);
}

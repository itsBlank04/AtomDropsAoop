package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.RepairMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RepairMediaRepository extends JpaRepository<RepairMedia, Long> {
    List<RepairMedia> findByRequestId(Long requestId);
}

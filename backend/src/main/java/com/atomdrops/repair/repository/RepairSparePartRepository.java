package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.RepairSparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RepairSparePartRepository extends JpaRepository<RepairSparePart, Long> {
    List<RepairSparePart> findByBookingId(Long bookingId);
}

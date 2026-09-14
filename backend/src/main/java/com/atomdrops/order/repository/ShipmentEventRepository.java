package com.atomdrops.order.repository;
import com.atomdrops.order.model.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {
    List<ShipmentEvent> findByShipmentIdOrderByOccurredAtDesc(Long shipmentId);
}

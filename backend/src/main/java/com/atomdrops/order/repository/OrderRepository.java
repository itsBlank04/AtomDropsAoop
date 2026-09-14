package com.atomdrops.order.repository;
import com.atomdrops.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByIdAndCustomerId(Long id, Long customerId);
}

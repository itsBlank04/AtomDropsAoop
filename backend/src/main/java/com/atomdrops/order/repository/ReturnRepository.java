package com.atomdrops.order.repository;
import com.atomdrops.order.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReturnRepository extends JpaRepository<Return, Long> {
    List<Return> findByCustomerId(Long customerId);
    List<Return> findByStatus(String status);
}

package com.atomdrops.order.repository;

import com.atomdrops.order.model.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByOrderId(Long orderId);
    List<Invoice> findByOrderIdIn(List<Long> orderIds);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}

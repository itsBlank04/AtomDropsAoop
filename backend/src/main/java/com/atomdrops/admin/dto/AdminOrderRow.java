package com.atomdrops.admin.dto;

import com.atomdrops.order.model.Invoice;
import com.atomdrops.order.model.Order;
import com.atomdrops.payment.model.Payment;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Read-only projection of an order for the admin Orders tab.
 *
 * <p>Tracking only: order status plus the payment method (COD / bKash / Nagad / card)
 * and whether the money has actually landed. The admin does not act on orders here, so
 * no mutation fields are exposed. Payment and invoice are resolved in a single batch
 * query by the caller and passed in, avoiding a lookup per order.
 */
public record AdminOrderRow(
        Long id,
        String status,
        BigDecimal subtotalBdt,
        BigDecimal shippingFeeBdt,
        BigDecimal discountBdt,
        BigDecimal taxBdt,
        BigDecimal totalBdt,
        int itemCount,
        String customerName,
        String customerEmail,
        String paymentMethod,
        String paymentStatus,
        String invoiceNumber,
        String invoiceStatus,
        Instant createdAt) {

    public static AdminOrderRow from(Order o, Payment payment, Invoice invoice) {
        var customer = o.getCustomer();
        var items = o.getItems();

        return new AdminOrderRow(
                o.getId(),
                o.getStatus(),
                o.getSubtotalBdt(),
                o.getShippingFeeBdt(),
                o.getDiscountBdt(),
                o.getTaxBdt(),
                o.getTotalBdt(),
                items != null ? items.size() : 0,
                customer != null ? customer.getDisplayName() : null,
                customer != null ? customer.getEmail() : null,
                payment != null ? payment.getMethod() : null,
                payment != null ? payment.getStatus() : null,
                invoice != null ? invoice.getInvoiceNumber() : null,
                invoice != null ? invoice.getStatus() : null,
                o.getCreatedAt());
    }
}

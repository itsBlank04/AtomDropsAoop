package com.atomdrops.product.repository;

import com.atomdrops.product.model.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVendorId(Long vendorId);
    long countByVendorId(Long vendorId);
    List<Product> findByShopId(Long shopId);
    long countByShopId(Long shopId);
    Page<Product> findByStatus(String status, Pageable pageable);

    /**
     * Admin listing: fetches every relation the admin product view renders
     * (pictures, category, vendor, shop and the shop's owner) in one query so the
     * dashboard does not trigger a query per product per relation.
     */
    @Query("SELECT DISTINCT p FROM Product p "
            + "LEFT JOIN FETCH p.images "
            + "LEFT JOIN FETCH p.category "
            + "LEFT JOIN FETCH p.vendor "
            + "LEFT JOIN FETCH p.shop "
            + "LEFT JOIN FETCH p.shop.vendor "
            + "ORDER BY p.id DESC")
    List<Product> findAllForAdmin();

    @Query(value = "SELECT * FROM products p WHERE p.deleted_at IS NULL AND p.status = 'ACTIVE' "
        + "AND (:search IS NULL OR LOWER(p.name::text) LIKE LOWER(CONCAT('%', :search, '%'))) "
        + "AND (:category IS NULL OR p.category_id = :category) "
        + "AND (:minPrice IS NULL OR p.price_bdt >= :minPrice) "
        + "AND (:maxPrice IS NULL OR p.price_bdt <= :maxPrice) "
        + "ORDER BY p.created_at DESC",
        countQuery = "SELECT count(*) FROM products p WHERE p.deleted_at IS NULL AND p.status = 'ACTIVE' "
        + "AND (:search IS NULL OR LOWER(p.name::text) LIKE LOWER(CONCAT('%', :search, '%'))) "
        + "AND (:category IS NULL OR p.category_id = :category) "
        + "AND (:minPrice IS NULL OR p.price_bdt >= :minPrice) "
        + "AND (:maxPrice IS NULL OR p.price_bdt <= :maxPrice)",
        nativeQuery = true)
    Page<Product> searchProducts(@Param("search") String search,
        @Param("category") Long category,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable);
}

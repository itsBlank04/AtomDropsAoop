package com.atomdrops.admin.dto;

import com.atomdrops.product.model.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Read-only projection of a product for the admin Marketplace tab.
 *
 * <p>Carries the full detail set an admin needs to review a listing, including the
 * primary picture, the owning shop, and the shop owner's user id. Built from a single
 * fetch-join query so the admin list does not issue a query per relation.
 */
public record AdminProductRow(
        Long id,
        String name,
        String description,
        BigDecimal priceBdt,
        String status,
        String shippingType,
        int viewCount,
        String imageUrl,
        List<String> imageUrls,
        Long categoryId,
        String categoryName,
        Long vendorId,
        String vendorName,
        Long shopId,
        String shopName,
        String shopSlug,
        String shopLogoUrl,
        Long shopOwnerId,
        String shopOwnerName,
        Instant createdAt) {

    public static AdminProductRow from(Product p) {
        var shop = p.getShop();
        var shopOwner = shop != null ? shop.getVendor() : null;
        var vendor = p.getVendor();
        var category = p.getCategory();

        var imageUrls = p.getImages().stream()
                .map(img -> img.getImageUrl())
                .filter(url -> url != null && !url.isBlank())
                .toList();

        return new AdminProductRow(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPriceBdt(),
                p.getStatus(),
                p.getShippingType(),
                p.getViewCount(),
                imageUrls.isEmpty() ? null : imageUrls.get(0),
                imageUrls,
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                vendor != null ? vendor.getId() : null,
                vendor != null ? vendor.getDisplayName() : null,
                shop != null ? shop.getId() : null,
                shop != null ? shop.getName() : null,
                shop != null ? shop.getSlug() : null,
                shop != null ? shop.getLogoUrl() : null,
                shopOwner != null ? shopOwner.getId() : null,
                shopOwner != null ? shopOwner.getDisplayName() : null,
                p.getCreatedAt());
    }
}

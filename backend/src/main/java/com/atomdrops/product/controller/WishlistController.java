package com.atomdrops.product.controller;

import com.atomdrops.security.SecurityConfig;
import com.atomdrops.product.model.Product;
import com.atomdrops.user.model.User;
import com.atomdrops.product.model.Wishlist;
import com.atomdrops.product.repository.ProductRepository;
import com.atomdrops.user.repository.UserRepository;
import com.atomdrops.product.repository.WishlistRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistController(WishlistRepository wishlistRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Wishlist> listWishlist(HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
        return wishlistRepository.findByUserId(userId);
    }

    @PostMapping("/{productId}")
    public Wishlist addToWishlist(@PathVariable Long productId, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        return wishlistRepository.save(wishlist);
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public String removeFromWishlist(@PathVariable Long productId, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
        return "ok";
    }
}

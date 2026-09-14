package com.atomdrops.product.service;

import com.atomdrops.order.model.OrderItem;
import com.atomdrops.product.model.Product;
import com.atomdrops.repair.model.RepairBooking;
import com.atomdrops.product.model.Review;
import com.atomdrops.user.model.User;
import com.atomdrops.order.repository.OrderItemRepository;
import com.atomdrops.product.repository.ProductRepository;
import com.atomdrops.repair.repository.RepairBookingRepository;
import com.atomdrops.product.repository.ReviewRepository;
import com.atomdrops.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final RepairBookingRepository repairBookingRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, RepairBookingRepository repairBookingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.repairBookingRepository = repairBookingRepository;
    }

    @Transactional
    public Review createReview(Long reviewerId, Long revieweeId, Long productId, Long orderItemId, Long bookingId, int rating, String comment) {
        User reviewer = userRepository.findById(reviewerId)
            .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));
        User reviewee = userRepository.findById(revieweeId)
            .orElseThrow(() -> new IllegalArgumentException("Reviewee not found"));

        Review review = new Review();
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(rating);
        review.setComment(comment);

        if (productId != null) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            review.setProduct(product);
        }

        if (orderItemId != null) {
            OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));
            review.setOrderItem(orderItem);
        }

        if (bookingId != null) {
            RepairBooking booking = repairBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Repair booking not found"));
            review.setBooking(booking);
        }

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForUser(Long revieweeId) {
        return reviewRepository.findByRevieweeId(revieweeId);
    }
}

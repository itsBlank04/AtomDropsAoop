package com.atomdrops.product.controller;

import com.atomdrops.security.SecurityConfig;
import com.atomdrops.product.model.Product;
import com.atomdrops.product.model.ProductQuestion;
import com.atomdrops.user.model.User;
import com.atomdrops.product.repository.ProductQuestionRepository;
import com.atomdrops.product.repository.ProductRepository;
import com.atomdrops.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/questions")
public class ProductQuestionController {

    private final ProductQuestionRepository questionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductQuestionController(ProductQuestionRepository questionRepository,
            ProductRepository productRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<ProductQuestion> getQuestions(@PathVariable Long productId) {
        return questionRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @PostMapping
    public ProductQuestion askQuestion(@PathVariable Long productId,
            @RequestBody Map<String, String> body, HttpSession session) {
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        User asker = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ProductQuestion q = new ProductQuestion();
        q.setProduct(product);
        q.setAsker(asker);
        q.setQuestion(body.get("question"));
        return questionRepository.save(q);
    }

    @PutMapping("/{questionId}/answer")
    public ProductQuestion answerQuestion(@PathVariable Long productId,
            @PathVariable Long questionId, @RequestBody Map<String, String> body,
            HttpSession session) {
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        ProductQuestion q = questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        if (!q.getProduct().getVendor().getId().equals(userId))
            throw new SecurityException("Only the product vendor can answer");
        User answerer = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        q.setAnswer(body.get("answer"));
        q.setAnsweredBy(answerer);
        q.setAnsweredAt(Instant.now());
        return questionRepository.save(q);
    }
}

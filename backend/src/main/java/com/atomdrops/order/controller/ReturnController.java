package com.atomdrops.order.controller;

import com.atomdrops.security.SecurityConfig;
import com.atomdrops.order.model.OrderItem;
import com.atomdrops.order.model.Return;
import com.atomdrops.user.model.User;
import com.atomdrops.order.repository.OrderItemRepository;
import com.atomdrops.order.repository.ReturnRepository;
import com.atomdrops.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    private final ReturnRepository returnRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public ReturnController(ReturnRepository returnRepository, UserRepository userRepository,
            OrderItemRepository orderItemRepository) {
        this.returnRepository = returnRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping
    public Return create(@RequestBody Map<String, Object> body, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        User customer = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!body.containsKey("orderItemId")) throw new IllegalArgumentException("orderItemId is required");
        OrderItem orderItem = orderItemRepository.findById(Long.valueOf(body.get("orderItemId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));
        if (orderItem.getOrder() == null || orderItem.getOrder().getCustomer() == null
                || !orderItem.getOrder().getCustomer().getId().equals(userId)) {
            throw new SecurityException("Not your order item");
        }
        Return returnRequest = new Return();
        returnRequest.setCustomer(customer);
        returnRequest.setOrderItem(orderItem);
        returnRequest.setReason((String) body.get("reason"));
        return returnRepository.save(returnRequest);
    }

    @GetMapping("/mine")
    public List<Return> getMine(HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        return returnRepository.findByCustomerId(userId);
    }

    @GetMapping("/{id}")
    public Return getOne(@PathVariable Long id, HttpSession session) {
        if (!SecurityConfig.hasRole("CUSTOMER")) throw new SecurityException("Customer access required");
        Long userId = SecurityConfig.getSessionUserId();
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        Return returnRequest = returnRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Return not found"));
        if (!returnRequest.getCustomer().getId().equals(userId)) throw new IllegalArgumentException("Not your return");
        return returnRequest;
    }
}

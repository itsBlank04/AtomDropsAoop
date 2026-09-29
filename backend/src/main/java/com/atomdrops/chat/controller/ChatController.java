package com.atomdrops.chat.controller;

import com.atomdrops.security.SecurityConfig;
import com.atomdrops.chat.model.Conversation;
import com.atomdrops.chat.model.ConversationMember;
import com.atomdrops.chat.model.Message;
import com.atomdrops.order.model.Order;
import com.atomdrops.order.model.OrderItem;
import com.atomdrops.user.model.User;
import com.atomdrops.chat.repository.ConversationMemberRepository;
import com.atomdrops.chat.repository.ConversationRepository;
import com.atomdrops.chat.repository.MessageRepository;
import com.atomdrops.order.repository.OrderRepository;
import com.atomdrops.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api", "/api/chat"})
public class ChatController {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    public ChatController(ConversationRepository conversationRepository,
            ConversationMemberRepository conversationMemberRepository,
            MessageRepository messageRepository, UserRepository userRepository,
            OrderRepository orderRepository,
            EntityManager entityManager) {
        this.conversationRepository = conversationRepository;
        this.conversationMemberRepository = conversationMemberRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.entityManager = entityManager;
    }

    private Long getUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = SecurityConfig.getSessionUserId();
        }
        return userId;
    }

    @GetMapping("/conversations")
    public List<Map<String, Object>> listConversations(
            @RequestParam(required = false) String type,
            HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        List<ConversationMember> members = conversationMemberRepository.findByUserId(userId);
        return members.stream()
            .filter(m -> type == null || type.equals(m.getConversation().getType()))
            .map(m -> {
                Conversation conv = m.getConversation();
                List<ConversationMember> allMembers = conversationMemberRepository.findByConversationId(conv.getId());
                ConversationMember otherMember = allMembers.stream()
                    .filter(cm -> !cm.getUser().getId().equals(userId))
                    .findFirst().orElse(null);
                User otherUser = otherMember != null ? otherMember.getUser() : null;
                Message lastMsg = messageRepository.findTopByConversationIdOrderByCreatedAtDesc(conv.getId()).orElse(null);
                long unreadCount = messageRepository.countByConversationIdAndIsReadFalseAndSenderIdNot(conv.getId(), userId);

                Map<String, Object> result = new HashMap<>();
                result.put("id", conv.getId());
                result.put("type", conv.getType());
                result.put("entityType", conv.getEntityType());
                result.put("entityId", conv.getEntityId());
                result.put("title", conv.getTitle());
                result.put("status", conv.getStatus());
                result.put("createdAt", conv.getCreatedAt());
                if (otherUser != null) {
                    result.put("otherUserId", otherUser.getId());
                    result.put("otherUserName", otherUser.getDisplayName());
                    result.put("otherUserAvatar", otherUser.getAvatarUrl() != null ? otherUser.getAvatarUrl() : "");
                }
                if (lastMsg != null) {
                    result.put("lastMessage", lastMsg.getBody());
                    result.put("lastMessageAt", lastMsg.getCreatedAt());
                    result.put("lastMessageSenderId", lastMsg.getSender().getId());
                    result.put("lastMessageType", lastMsg.getMessageType());
                }
                result.put("unreadCount", unreadCount);
                return result;
            }).collect(Collectors.toList());
    }

    /** Aggregate unread message count across all conversations */
    @GetMapping("/unread-count")
    public Map<String, Object> getUnreadCount(HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        List<ConversationMember> members = conversationMemberRepository.findByUserId(userId);
        long total = 0;
        for (ConversationMember member : members) {
            Conversation conv = member.getConversation();
            total += messageRepository.countByConversationIdAndIsReadFalseAndSenderIdNot(conv.getId(), userId);
        }
        return Map.of("total", total);
    }


    /** Create or get order conversation (auto-created on checkout, accessible here) */
    @GetMapping("/conversations/order/{orderId}")
    public Map<String, Object> getOrderConversation(@PathVariable Long orderId, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        // Verify user is buyer or vendor of this order
        boolean isBuyer = order.getCustomer().getId().equals(userId);
        boolean isVendor = order.getItems().stream()
            .anyMatch(item -> item.getProduct() != null
                && item.getProduct().getVendor() != null
                && item.getProduct().getVendor().getId().equals(userId));
        if (!isBuyer && !isVendor) {
            throw new IllegalArgumentException("Not a participant in this order");
        }
        // Find existing conversation
        List<ConversationMember> myMemberships = conversationMemberRepository.findByUserId(userId);
        for (ConversationMember member : myMemberships) {
            Conversation c = member.getConversation();
            if ("ORDER".equals(c.getType()) && orderId.equals(c.getEntityId())) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", c.getId());
                result.put("existing", true);
                result.put("type", c.getType());
                result.put("title", c.getTitle());
                result.put("status", c.getStatus());
                return result;
            }
        }
        throw new IllegalArgumentException("Order conversation not found");
    }

    @GetMapping("/conversations/{id}/messages")
    public List<Map<String, Object>> getMessages(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "50") int limit,
            HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        if (!conversationMemberRepository.existsByConversationIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("Not a member of this conversation");
        }
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(id);
        messages.stream()
            .filter(msg -> !msg.getSender().getId().equals(userId) && !msg.isRead())
            .forEach(msg -> { msg.setRead(true); messageRepository.save(msg); });
        return messages.stream().map(msg -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", msg.getId());
            m.put("messageType", msg.getMessageType());
            m.put("body", msg.getBody());
            m.put("attachmentUrl", msg.getAttachmentUrl());
            m.put("attachmentName", msg.getAttachmentName());
            m.put("senderId", msg.getSender().getId());
            m.put("senderName", msg.getSender().getDisplayName());
            m.put("senderAvatar", msg.getSender().getAvatarUrl() != null ? msg.getSender().getAvatarUrl() : "");
            m.put("isMine", msg.getSender().getId().equals(userId));
            m.put("isRead", msg.isRead());
            m.put("createdAt", msg.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
    }

    @PostMapping("/conversations/{id}/messages")
    public Map<String, Object> sendMessage(@PathVariable Long id,
            @RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) throw new IllegalArgumentException("Not authenticated");
        if (!conversationMemberRepository.existsByConversationIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("Not a member of this conversation");
        }
        User sender = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Conversation conversation = conversationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageType("TEXT");
        message.setBody((String) body.get("body"));
        message = messageRepository.save(message);
        conversation.setLastMessageAt(message.getCreatedAt());
        conversationRepository.save(conversation);
        Map<String, Object> result = new HashMap<>();
        result.put("id", message.getId());
        result.put("messageType", message.getMessageType());
        result.put("body", message.getBody());
        result.put("senderId", sender.getId());
        result.put("senderName", sender.getDisplayName());
        result.put("senderAvatar", sender.getAvatarUrl() != null ? sender.getAvatarUrl() : "");
        result.put("isMine", true);
        result.put("createdAt", message.getCreatedAt());
        return result;
    }

    @Transactional
    @DeleteMapping("/conversations")
    public String deleteAllConversations() {
        entityManager.createNativeQuery("DELETE FROM messages").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM conversation_members").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM conversations").executeUpdate();
        return "ok";
    }
}

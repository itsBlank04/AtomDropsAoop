package com.atomdrops.chat.repository;
import com.atomdrops.chat.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}

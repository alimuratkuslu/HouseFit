package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderId(Long senderId);
}

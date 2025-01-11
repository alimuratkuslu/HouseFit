package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.ChatMessage;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.ChatMessageRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatService(ChatMessageRepository chatMessageRepository, UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    public ChatMessage sendMessage(Long receiverId, String content) {
        String senderUsername = getLoggedInUsername();
        User user = userService.findByUsername(senderUsername);
        User receiver = userService.findById(receiverId);

        ChatMessage message = ChatMessage.builder()
                .sender(user)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory() {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username);
        return chatMessageRepository.findBySenderId(user.getId());
    }

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}

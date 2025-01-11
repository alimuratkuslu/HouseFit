package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.ChatMessage;
import com.alikuslu.housefit.demo.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestParam Long receiverId, @RequestParam String content) {
        ChatMessage message = chatService.sendMessage(receiverId, content);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory() {
        List<ChatMessage> messages = chatService.getChatHistory();
        return ResponseEntity.ok(messages);
    }
}

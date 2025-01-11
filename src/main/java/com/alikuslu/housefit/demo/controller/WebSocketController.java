package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.ChatMessage;
import com.alikuslu.housefit.demo.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final ChatService chatService;

    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return chatService.sendMessage(message.getReceiver().getId(), message.getContent());
    }
}

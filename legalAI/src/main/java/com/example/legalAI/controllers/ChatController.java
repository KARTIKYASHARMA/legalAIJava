package com.example.legalAI.controllers;

import com.example.legalAI.entities.ChatMessage;
import com.example.legalAI.entities.Users;
import com.example.legalAI.services.ChatService;
import com.example.legalAI.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    // Display chat page (form-login based)
    @GetMapping
    public String chatPage(Authentication auth, Model model) {
        Users user = userService.getByUsername(auth.getName());
        List<ChatMessage> messages = chatService.getUserChats(user);
        model.addAttribute("messages", messages);
        model.addAttribute("username", user.getUsername());
        return "chat";
    }

    // API endpoint for sending messages (AJAX)
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendMessage(
            @RequestBody Map<String, String> payload,
            Authentication auth) {

        String query = payload.get("message");
        Users user = userService.getByUsername(auth.getName());

        // 1️⃣ Save user's message
        chatService.processUserMessage(user, query);

        // 2️⃣ Call GPT microservice
        String aiReply;
        try {
            String gptUrl = "http://localhost:8000/ask?query=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8);

            Map<String, String> response = restTemplate.getForObject(gptUrl, Map.class);
            aiReply = response.get("answer");

        } catch (Exception e) {
            e.printStackTrace();
            aiReply = "Sorry, I couldn't reach the AI service.";
        }

        // 3️⃣ Save AI's reply
        chatService.processBotMessage(user, aiReply);

        Map<String, String> result = new HashMap<>();
        result.put("reply", aiReply);
        return ResponseEntity.ok(result);
    }
}
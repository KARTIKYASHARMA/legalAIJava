package com.example.legalAI.services;

import com.example.legalAI.entities.ChatMessage;

import com.example.legalAI.entities.Users;
import com.example.legalAI.repositories.ChatRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // Save user's message
    public ChatMessage processUserMessage(Users user, String query) {
        ChatMessage chat = ChatMessage.builder()
                .users(user)
                .message(query)
                .isBot(false) // user message
                .timestamp(LocalDateTime.now())
                .build();

        return chatRepository.save(chat);
    }

    // Save bot's response
    public ChatMessage processBotMessage(Users user, String aiReply) {
        ChatMessage botMessage = ChatMessage.builder()
                .users(user)
                .message(aiReply)
                .isBot(true) // bot message
                .timestamp(LocalDateTime.now())
                .build();

        return chatRepository.save(botMessage);
    }

    // Get all messages for a user
    public List<ChatMessage> getUserChats(Users user) {
        return chatRepository.findByUsersOrderByTimestampAsc(user);
    }
}
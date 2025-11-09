package com.example.legalAI.repositories;

import com.example.legalAI.entities.ChatMessage;
import com.example.legalAI.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUsersOrderByTimestampAsc(Users user);
}
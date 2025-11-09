package com.example.legalAI.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT") // or LONGTEXT if really long messages
    private String message;
    private boolean isBot; // true = bot, false = user
    private LocalDateTime timestamp;

    @ManyToOne
    //@JoinColumn(name = "user_id")
    private Users users;
}
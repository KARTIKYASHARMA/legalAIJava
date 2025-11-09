package com.example.legalAI.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // avoid reserved keyword "user"
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email; // ✅ ADD THIS

    private String password;

    @Column(nullable = false)
    private String role = "USER";
}
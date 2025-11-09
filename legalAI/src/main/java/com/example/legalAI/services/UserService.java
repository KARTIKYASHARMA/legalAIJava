package com.example.legalAI.services;


import com.example.legalAI.entities.Users;
import com.example.legalAI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users registerUser(Users u) {
        // simple check: username uniqueness
        Optional<Users> existing = userRepository.findByUsername(u.getUsername());
        if (existing.isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepository.save(u);
    }

    public Users getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // convenience method used by CustomUserDetailsService
    public Optional<Users> findOptionalByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

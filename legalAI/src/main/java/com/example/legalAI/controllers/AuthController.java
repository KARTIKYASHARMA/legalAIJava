package com.example.legalAI.controllers;

import com.example.legalAI.entities.Users;
import com.example.legalAI.services.JwtService;
import com.example.legalAI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // DTOs (inner static classes) — or move to their own files if you prefer
    public static record AuthRequest(String username, String password) {}
    public static record AuthResponse(String token) {}

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req) {
        Users u = new Users();
        u.setUsername(req.username());
        u.setPassword(req.password());
        userService.registerUser(u);
        // authenticate immediately to return token
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        String token = jwtService.generateToken(auth);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        String token = jwtService.generateToken(auth);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

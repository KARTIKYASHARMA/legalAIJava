package com.example.legalAI.service;



import com.example.legalAI.entities.Users;
import com.example.legalAI.repositories.UserRepository;
import com.example.legalAI.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = Users.builder()
                .username("john123")
                .password("plainpassword")
                .email("john@email.com")
                .build();
    }

    @Test
    void registerUser_ShouldRegister_WhenUsernameUnique() {
        when(userRepository.findByUsername("john123")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        Users result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).findByUsername("john123");
        verify(passwordEncoder, times(1)).encode("plainpassword");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void registerUser_ShouldThrow_WhenUsernameExists() {
        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(testUser));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(testUser));

        assertEquals("Username already taken", ex.getMessage());
        verify(userRepository, times(1)).findByUsername("john123");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getByUsername_ShouldReturnUser_WhenFound() {
        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(testUser));

        Users result = userService.getByUsername("john123");

        assertNotNull(result);
        assertEquals("john123", result.getUsername());

        verify(userRepository, times(1)).findByUsername("john123");
    }

    @Test
    void getByUsername_ShouldThrow_WhenNotFound() {
        when(userRepository.findByUsername("john123")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.getByUsername("john123"));

        assertEquals("User not found: john123", ex.getMessage());
        verify(userRepository, times(1)).findByUsername("john123");
    }

    @Test
    void findOptionalByUsername_ShouldReturnOptional() {
        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(testUser));

        Optional<Users> result = userService.findOptionalByUsername("john123");

        assertTrue(result.isPresent());
        assertEquals("john123", result.get().getUsername());

        verify(userRepository, times(1)).findByUsername("john123");
    }
}

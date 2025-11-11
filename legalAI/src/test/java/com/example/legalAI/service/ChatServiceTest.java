package com.example.legalAI.service;




import com.example.legalAI.entities.ChatMessage;
import com.example.legalAI.entities.Users;
import com.example.legalAI.repositories.ChatRepository;
import com.example.legalAI.services.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    private Users mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = Users.builder()
                .id(1L)
                .email("test@example.com")
                .build();
    }

    @Test
    void processUserMessage_ShouldSaveUserMessage() {
        String query = "Hello";

        ChatMessage message = ChatMessage.builder()
                .users(mockUser)
                .message(query)
                .isBot(false)
                .timestamp(LocalDateTime.now())
                .build();

        when(chatRepository.save(any(ChatMessage.class))).thenReturn(message);

        ChatMessage result = chatService.processUserMessage(mockUser, query);

        assertNotNull(result);
        assertEquals(query, result.getMessage());
        assertFalse(result.isBot());
        verify(chatRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void processBotMessage_ShouldSaveBotMessage() {
        String aiReply = "Hello, how can I help?";

        ChatMessage message = ChatMessage.builder()
                .users(mockUser)
                .message(aiReply)
                .isBot(true)
                .timestamp(LocalDateTime.now())
                .build();

        when(chatRepository.save(any(ChatMessage.class))).thenReturn(message);

        ChatMessage result = chatService.processBotMessage(mockUser, aiReply);

        assertNotNull(result);
        assertEquals(aiReply, result.getMessage());
        assertTrue(result.isBot());
        verify(chatRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void getUserChats_ShouldReturnUserChatList() {
        List<ChatMessage> chatList = new ArrayList<>();

        ChatMessage msg1 = ChatMessage.builder()
                .users(mockUser)
                .message("Hi")
                .isBot(false)
                .timestamp(LocalDateTime.now())
                .build();

        chatList.add(msg1);

        when(chatRepository.findByUsersOrderByTimestampAsc(mockUser)).thenReturn(chatList);

        List<ChatMessage> result = chatService.getUserChats(mockUser);

        assertEquals(1, result.size());
        assertEquals("Hi", result.get(0).getMessage());
        verify(chatRepository, times(1)).findByUsersOrderByTimestampAsc(mockUser);
    }
}

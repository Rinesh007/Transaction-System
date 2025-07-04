package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Test
    void testCheckBalance() {
        // Mock user and repository
        UserRepository mockRepo = mock(UserRepository.class);
        User mockUser = new User("rinesh", "pass123", "user", 500.0);

        when(mockRepo.findByUsername("rinesh")).thenReturn(mockUser);

        // Inject into service
        UserService userService = new UserService();
        // Use reflection to inject mock since fields are private and no constructor
        java.lang.reflect.Field repoField;
        try {
            repoField = UserService.class.getDeclaredField("userRepo");
            repoField.setAccessible(true);
            repoField.set(userService, mockRepo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        double balance = userService.checkBalance("rinesh");
        assertEquals(500.0, balance, 0.01);
    }
}

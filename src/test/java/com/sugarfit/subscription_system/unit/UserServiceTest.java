package com.sugarfit.subscription_system.unit;

import com.example.subscription.entity.User;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@gmail.com");
    }
}

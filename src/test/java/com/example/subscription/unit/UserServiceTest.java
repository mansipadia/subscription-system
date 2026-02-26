package com.example.subscription.unit;

import com.example.subscription.DTO.CreateUserRequest;
import com.example.subscription.DTO.UserResponse;
import com.example.subscription.entity.User;
import com.example.subscription.exception.ConflictException;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success(){
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Rutvi");
        request.setEmail("rutvi@test.com");

        when(userRepository.findByEmail("rutvi@test.com")).thenReturn(Optional.empty());

        when((userRepository.save(any(User.class)))).thenAnswer(invocation->invocation.getArgument(0));

        UserResponse response = userService.saveUserData(request);

        assertEquals("Rutvi",response.getName());
        assertEquals("rutvi@test.com",response.getEmail());

        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_shouldThrow(){

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Rutvi");
        request.setEmail("rutvi@test.com");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("rutvi@test.com");

        when(userRepository.findByEmail("rutvi@test.com"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class,
                () -> userService.saveUserData(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_success() {

        User user = new User();
        user.setId(1L);
        user.setName("Rutvi");
        user.setEmail("rutvi@test.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserDataById(1L);

        assertEquals("Rutvi", response.getName());
        assertEquals("rutvi@test.com", response.getEmail());
    }

    @Test
    void getUserById_notFound_shouldThrow() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserDataById(1L));
    }

    @Test
    void getAllUsers_success() {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Rutvi");
        user1.setEmail("rutvi@test.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Mansi");
        user2.setEmail("mansi@test.com");

        when(userRepository.findAll())
                .thenReturn(java.util.List.of(user1, user2));

        var users = userService.getUserData();

        assertEquals(2, users.size());
    }

}

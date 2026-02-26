package com.example.subscription.controller;

import com.example.subscription.DTO.CreateUserRequest;
import com.example.subscription.DTO.UserResponse;
import com.example.subscription.exception.ConflictException;
import com.example.subscription.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);

        UserController userController = new UserController(userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void createUser_success() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Rutvi");
        request.setEmail("rutvi@test.com");
        request.setPassword("123456");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Rutvi");
        response.setEmail("rutvi@test.com");

        Mockito.when(userService.saveUserData(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rutvi"))
                .andExpect(jsonPath("$.email").value("rutvi@test.com"));
    }

}
package com.example.subscription.service;

import com.example.subscription.DTO.CreateUserRequest;
import com.example.subscription.DTO.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse saveUserData(CreateUserRequest request);
    List<UserResponse> getUserData();
    UserResponse getUserDataById(Long id);
}

package com.example.subscription.service.impl;

import com.example.subscription.DTO.CreateUserRequest;
import com.example.subscription.DTO.UserResponse;
import com.example.subscription.entity.User;
import com.example.subscription.exception.ConflictException;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponse saveUserData(CreateUserRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ConflictException("Email is already Exist...");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreated_at());
        return response;
    }

    @Override
    public List<UserResponse> getUserData() {

        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public UserResponse getUserDataById(Long id) {

        User user =userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found : "+id));
        return mapToResponse(user);
    }



}

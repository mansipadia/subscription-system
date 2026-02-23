package com.example.subscription.service.impl;

import com.example.subscription.entity.User;
import com.example.subscription.repository.UserRepository;
import com.example.subscription.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }

    public boolean emailExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User saveUserData(User user){
        if (!emailExists(user.getEmail()))
           return userRepository.save(user);
        else
            throw new RuntimeException("Email is already Exist...");
    }

    @Override
    public List<User> getUserData() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserDataById(Integer id) {
        return userRepository.findById(id);
    }



}

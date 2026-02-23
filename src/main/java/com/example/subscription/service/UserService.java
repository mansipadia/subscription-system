package com.example.subscription.service;

import com.example.subscription.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUserData(User user);
    List<User> getUserData();
    Optional<User> getUserDataById(Integer id);
}

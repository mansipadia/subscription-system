package com.example.subscription.repository;

import com.example.subscription.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save user successfully")
    void saveUser_success() {

        User user = new User();
        user.setName("Rutvi");
        user.setEmail("rutvi@test.com");
        user.setPassword("123456");

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("rutvi@test.com");
    }

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_success() {

        User user = new User();
        user.setName("Rutvi");
        user.setEmail("rutvi@test.com");
        user.setPassword("123456");

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("rutvi@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rutvi");
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void findByEmail_notFound() {

        Optional<User> found = userRepository.findByEmail("unknown@test.com");

        assertThat(found).isEmpty();
    }

}
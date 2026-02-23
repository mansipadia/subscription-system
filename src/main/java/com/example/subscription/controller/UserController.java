package com.example.subscription.controller;

import com.example.subscription.entity.User;
import com.example.subscription.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user){
        try{
            userService.saveUserData(user);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){

        try{
            return new ResponseEntity<>(userService.getUserData(), HttpStatusCode.valueOf(200));

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }


    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("id") Integer id){

        try{
            return new ResponseEntity<>(userService.getUserDataById(id),HttpStatusCode.valueOf(200));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }

    }

}

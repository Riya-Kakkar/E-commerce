package com.E_commerce.Service;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.UserRequest;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //used in orderservice
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

   /* public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
       *//* User user = userRepository.getUserByUserName(email);*//*
        return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }*/


    public ResponseEntity<String>  registerUser(UserRequest userRequest ) {

        try {

            /* Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());*/
            Optional<User>  existingUser = userRepository.findByUsername(userRequest.getUsername());
            if (existingUser.isPresent()) {
                System.out.println("User already exists with this : '" + userRequest.getUsername() + "'....PLease Try Another One !......");
                return ResponseEntity.status(409).body("Username :- '" + userRequest.getUsername() + "' already exists...PLease Try Another One !.....");
            }

            existingUser = userRepository.findByEmail(userRequest.getEmail());
            if (existingUser.isPresent()) {
                System.out.println("Email already exists.");
                return ResponseEntity.status(409).body(existingUser + " --- their Email already exists.");
            }

            System.out.println("---------userRequest data from Postman----------- " +userRequest);

            String userRole = userRequest.getRole();
            if (!userRole.startsWith("ROLE_")) {
                userRole = "ROLE_" + userRole;
            }
            User user = new User();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRole(userRole);
            System.out.println("user data------ " +user);

            userRepository.save(user);

            System.out.println("User registered Successfully!. Add more....");
            return ResponseEntity.status(201).body("User registered Successfully!. Add more....");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during user registration.");
            return ResponseEntity.status(500).body("Error during user registration.");
        }
    }



    public Optional<User> authenticateUser(String username, String password) {
        System.out.println("Authenticate user: " + username);
        Optional<User> user = userRepository.findByUsername(username);
          if (user.isEmpty()) {
              System.out.println("No user found with username: " + username);
               return Optional.empty();
              }
        if (user.isPresent()) {
            User loginUser = user.get();
            if (passwordEncoder.matches(password, loginUser.getPassword())) {
                System.out.println("Authentication successful for user: " + username);
                return  Optional.of(loginUser);
            }
            else {
                System.out.println("Password does not matched for USERNAME: " +username + " , PLEASE WRITE THE CORRECT PASSWORD...");
            }
        }
            System.out.println("Authentication failed for USERNAME: " + username);
        return Optional.empty();
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String changePassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return  username;
    }

    public void updateProfile(String username, User updatedUser) {
        User user = getUserByUsername(username);
        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());
        userRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {

        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser.isPresent();
    }

}


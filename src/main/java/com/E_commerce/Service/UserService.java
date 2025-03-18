package com.E_commerce.Service;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.UserRequest;
import com.E_commerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    //used in orderservice
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }


    public ResponseEntity<String>  registerUser(UserRequest userRequest ) {
        System.out.println("----------- Registering User with Details ------------- ");
        System.out.println("USERNAME:- " + userRequest.getUsername());
        System.out.println("EMAIL:- " + userRequest.getEmail());
        System.out.println("PASSWORD:- " + userRequest.getPassword());
        System.out.println("ROLE:- " + userRequest.getRole());

        try {
            Optional<User>  existingUser = userRepository.findByUsername(userRequest.getUsername());
            if (existingUser.isEmpty()) {
                existingUser = userRepository.findByEmail(userRequest.getEmail());
                System.out.println("User exists  : " + existingUser );
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("User :- " + existingUser);
            }

            if (existingUser.isPresent()) {
                System.out.println("User already exists with this email: '" + userRequest.getEmail() + "'....Please Try Another One!");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Email '" + userRequest.getEmail() + "' already exists...Please try another one!");
            }


            System.out.println("---------userRequest data from Postman----------- " +userRequest);

            String userRole = userRequest.getRole();
            if (!userRole.startsWith("ROLE_")) {
                userRole = "ROLE_" + userRole;
            }

            User user = modelMapper.map(userRequest, User.class);

            // Encrypt the password and set the role
            user = new User(
                    user.id(),
                    user.username(),
                    userRequest.getEmail(),
                    passwordEncoder.encode(userRequest.getPassword()),
                    userRole,
                    true
            );
            userRepository.save(user);

            System.out.println("User registered Successfully!. Add more....");
            return ResponseEntity.status(201).body("User registered Successfully!. Add more....");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during user registration.");
            return ResponseEntity.status(500).body("Error during user registration.");
        }
    }

    // Authenticate a user (login)

    public Optional<User> authenticateUser(String email, String password) {
        System.out.println("Authenticate user by email : " + email);
        Optional<User> user = userRepository.findByEmail(email);
          if (user.isEmpty()) {
              System.out.println("No user found with email: " + email);
               return Optional.empty();
           }
        if (user.isPresent() && passwordEncoder.matches(password, user.get().password()) ) {
            System.out.println("Authentication successful for user: " + email);
            return user;
        }
            else {
                System.out.println("Password does not matched for email : " +email + " , PLEASE WRITE THE CORRECT PASSWORD...");
            }
            System.out.println("Authentication failed for email  : " + email);
        return Optional.empty();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Change password
    public String changePassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user = new User(user.id(), user.username(), user.email(),encryptedPassword , user.role(), user.enable());
        userRepository.save(user);
        return  username;
    }

    // Update profile
    public void updateProfile(String username,  User updatedUser) {
        User currentUser = getUserByUsername(username);



        User userToUpdate = new User(
                currentUser.id(),
                updatedUser.username() != null ? updatedUser.username() : currentUser.username(),
                updatedUser.email() != null ? updatedUser.email() : currentUser.email(),
                currentUser.password(),  // Keep the existing password
                currentUser.role(),      // Keep the existing role
                currentUser.enable()     // Keep the existing status
        );

        userRepository.save(userToUpdate);
    }

    public boolean isUsernameTaken(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser.isPresent();
    }

}


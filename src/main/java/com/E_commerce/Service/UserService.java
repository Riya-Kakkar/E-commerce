package com.E_commerce.Service;

import com.E_commerce.Entity.User;
import com.E_commerce.Helper.UserNotFoundException;
import com.E_commerce.Model.UserDTO;
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

    public ResponseEntity<String>  registerUser(UserDTO userDTO ) {

        Optional<User>  existingUser = userRepository.findByUsername(userDTO.username());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Username '" + userDTO.username() + "' already exists. Please try another one!");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Email '" + userDTO.email() + "' already exists. Please try another one!");
        }

        String userRole = userDTO.role();
        if (!userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole;
        }

        User user = modelMapper.map(userDTO, User.class);

        user = new User(user.getId(), userDTO.username(), userDTO.email(), passwordEncoder.encode(userDTO.password()), userRole, true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered Successfully!. Add more....");

    }

    //used in orderservice
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }


    // Authenticate a user (login)

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword()) ) {
            return user;
        }
        return Optional.empty();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    // Change password
    public String changePassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user = new User(user.getId(), user.getPassword(), user.getEmail(),encryptedPassword , user.getRole(), user.isEnable());
        userRepository.save(user);
        return  username;
    }

    // Update profile
    public void updateProfile(String username,  User updatedUser) {
        User currentUser = getUserByUsername(username);

        User userToUpdate = new User(
                currentUser.getId(),
                updatedUser.getUsername() != null ? updatedUser.getUsername() : currentUser.getUsername(),
                updatedUser.getEmail() != null ? updatedUser.getEmail() : currentUser.getEmail(),
                currentUser.getPassword(),
                currentUser.getRole(),
                currentUser.isEnable()
        );

        userRepository.save(userToUpdate);
    }

    public boolean isUsernameTaken(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser.isPresent();
    }

}


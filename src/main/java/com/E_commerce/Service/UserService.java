package com.E_commerce.Service;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.UserDTO;
import com.E_commerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username '" + userDTO.username() + "' already exists.");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email '" + userDTO.email() + "' already exists.");
        }

        String role = userDTO.role().startsWith("ROLE_") ? userDTO.role() : "ROLE_" + userDTO.role();

        User user = new User(0, userDTO.username(), userDTO.email(),
                passwordEncoder.encode(userDTO.password()), role, true);

        userRepository.save(user);
    }

    //used in orderservice
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // Change password
    public String changePassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user = new User(user.getId(), user.getUsername(), user.getEmail(),encryptedPassword , user.getRole(), user.isEnable());
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

}


package com.E_commerce.Config;

import com.E_commerce.Entity.User;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*@Override
    public UserDetails loadUserByUserEmail(String userName) throws UsernameNotFoundException {
        //  user by email from db
        Optional<User> userOptional = userRepository.findByEmail(userName);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Could not found User !!");
        }
        User user = userOptional.get();
     *//* return (UserDetails) user;*//*
        return new CustomUserDetails(user);
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username); // Find user by username from the DB
        if (byUsername.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = byUsername.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        System.out.println("Loaded user: " + user.getUsername() + " with role: " + user.getRole() + " having authorities: " + customUserDetails.getAuthorities());
        return new CustomUserDetails(user);

    }
}

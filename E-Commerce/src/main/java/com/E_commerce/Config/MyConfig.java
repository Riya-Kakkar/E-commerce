package com.E_commerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/user/register", "/user/login").permitAll()
                        .requestMatchers("/reviews/product/**", "/reviews/average/**").permitAll()

                        // Authenticated Endpoints
                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/reviews/add").authenticated()
                        .requestMatchers("/admin-dashboard/**").authenticated()

                        // Role-based Access Control
                        .requestMatchers("/reviews/markInappropriate/**", "/reviews/delete/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/seller/**").hasRole("SELLER")

                        // Secure all other endpoints
                        .requestMatchers("/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()) // API-based authentication (no UI)
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }


   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
                   .authorizeHttpRequests(auth->auth
                          *//* .requestMatchers("/user/register").permitAll()
                           .requestMatchers("/user/login").permitAll()*//*
                          *//* .requestMatchers("/user/register", "/user/login").authenticated()  // Require authentication for these endpoints*//*
//                           .requestMatchers("/user/register", "/user/login").permitAll()
//                           .requestMatchers("/user/**").authenticated()
                          *//* .requestMatchers("/user/update-profile").permitAll()*//*
                           .requestMatchers("/user/register", "/user/login").permitAll()
                                   .requestMatchers("/orders/**").authenticated() // Require login for orders
                                   .requestMatchers("/reviews/add").authenticated()
                                   .requestMatchers("/reviews/markInappropriate/**", "/reviews/delete/**").hasRole("ADMIN")
                                   .requestMatchers("/reviews/product/**", "/reviews/average/**").permitAll()
                                   .requestMatchers("/admin-dashboard/**").authenticated()
                                   .requestMatchers("/admin/**").hasRole("ADMIN")
                           .requestMatchers("/customer/**").hasRole("CUSTOMER")
                           .requestMatchers("/seller/**").hasRole("SELLER")
                           *//*.requestMatchers("/**").permitAll()*//*
                           .requestMatchers("/**").authenticated()
                           .anyRequest().authenticated()
                   )
                   .httpBasic(withDefaults())  //this is for api based authentication -- no view page
                   .csrf(csrf -> csrf.disable()
                   )
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }
*/
}

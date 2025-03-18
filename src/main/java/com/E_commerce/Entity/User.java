package com.E_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USER")
public record User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        int id,

        @NotBlank(message = "Name can not be empty!! ")
        @Size(min = 2, max = 20, message = "Name must be in between 2-20 characters...")
        String username,

        @Column(unique = true)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$", message = "Invalid Email !! ")
        String email,

        String password,
        String role,
        boolean enable
) {
}
















































/*
package com.E_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name can not be empty!! ")
    @Size(min = 2,max = 20,message = "Name must be in between 2-20 characters...")
    private String username;

    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$" , message = "Invalid Email !! ")
    private String email;
    private String password;
    private String role;
    private boolean enable;


    public User() {
    }

    public User(String username, String email, String encode, String role) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.role = role;
        this.enable = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
       this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enable=" + enable +
                '}';
    }
}

*/

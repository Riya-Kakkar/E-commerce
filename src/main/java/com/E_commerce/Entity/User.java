package com.E_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @NotBlank(message = "Name can not be empty!! ")
        @Size(min = 2, max = 20, message = "Name must be in between 2-20 characters...")
        private String username;

        @Column(unique = true)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$", message = "Invalid Email !! ")
        private String email;

        @NotBlank(message = "Password cannot be empty!!")
        private String password;
        private String role;
        private boolean enable;

}

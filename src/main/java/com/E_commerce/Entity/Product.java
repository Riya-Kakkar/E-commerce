package com.E_commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name can not be empty!! ")
    private String name;
    @Column(length = 1000)
    private String description;
    private long price;
    private int stock;
    private String category;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
}


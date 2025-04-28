package com.E_commerce.Model;

import com.E_commerce.Entity.Product;
import lombok.Data;

@Data
public class UpdatedProductResponse {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;

    public UpdatedProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getCategory();
        this.imageUrl = product.getImageUrl();
    }

    @Override
    public String toString() {
        return "Your Product is Updated...\n" +
                "Details are----\n" +
                "id=" + id + ",\n" +
                "name=" + name + ",\n" +
                "description=" + description + ",\n" +
                "price=" + price + ",\n" +
                "stock=" + stock + ",\n" +
                "category=" + category + ",\n" +
                "imageUrl=" + imageUrl;
    }
}
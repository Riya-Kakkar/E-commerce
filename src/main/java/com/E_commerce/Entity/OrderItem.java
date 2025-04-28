package com.E_commerce.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id" , nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id" , nullable = false)
    @JsonBackReference
    private Order order;

    private int quantity;
    private long price;

    public OrderItem( Product product, Order order, int quantity, long price) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

}

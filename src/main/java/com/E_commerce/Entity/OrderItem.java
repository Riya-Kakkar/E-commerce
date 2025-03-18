package com.E_commerce.Entity;


import jakarta.persistence.*;
import jdk.jfr.Enabled;

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
    private Order order;

    private int quantity;
    private long price;

    public OrderItem( Product product, Order order, int quantity, long price) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}

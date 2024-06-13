package com.demo.storeweb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String phone;
    private String address;
    private LocalDateTime orderDate;
    private Integer rating; // Add rating field

    // Constructors, getters, and setters
    public UserOrder() {}

    public UserOrder(User user, Product product, String phone, String address, LocalDateTime orderDate, Integer rating) {
        this.user = user;
        this.product = product;
        this.phone = phone;
        this.address = address;
        this.orderDate = orderDate;
        this.rating = rating;
    }

    // Getters and setters omitted for brevity
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}

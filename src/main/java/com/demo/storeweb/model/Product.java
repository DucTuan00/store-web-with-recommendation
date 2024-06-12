package com.demo.storeweb.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String brand;
    private String category;
    private String price;

    @Column(columnDefinition = "TEXT") //Kiểu dữ liệu cột description là text
    private String description;
    private String imageFileName;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<UserOrder> orders;
}

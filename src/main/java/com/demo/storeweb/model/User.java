package com.demo.storeweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String favoriteCategories;
    
    @Transient
    private Set<String> favoriteCategoriesSet = new HashSet<>();

    @PostLoad
    private void postLoad() {
        if (favoriteCategories != null) {
            favoriteCategoriesSet = new HashSet<>(List.of(favoriteCategories.split(",")));
        }
    }

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        if (favoriteCategoriesSet != null) {
            favoriteCategories = String.join(",", favoriteCategoriesSet);
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserOrder> orders;
}

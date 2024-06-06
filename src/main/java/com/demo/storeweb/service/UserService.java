package com.demo.storeweb.service;

import com.demo.storeweb.model.User;
import com.demo.storeweb.dto.UserRegistrationDTO;
import com.demo.storeweb.repository.UserRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserRegistrationDTO registrationDTO) {
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());  // In real applications, encrypt the password
        user.setFavoriteCategories(registrationDTO.getFavoriteCategories());
        userRepository.save(user);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public Set<String> getCategories() {
        // This method should return the list of available categories
        return Set.of("Laptop", "PC", "Valorant"); // Example categories
    }
}

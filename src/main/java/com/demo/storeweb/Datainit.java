package com.demo.storeweb;

import com.demo.storeweb.model.User;
import com.demo.storeweb.repository.UserRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Datainit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setUsername("admin");
            defaultUser.setPassword("admin");
            defaultUser.setFavoriteCategories(Set.of("Electronics","Books","Clothing"));
            userRepository.save(defaultUser);
        }
    }
}

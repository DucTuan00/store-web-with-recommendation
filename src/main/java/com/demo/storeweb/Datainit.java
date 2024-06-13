package com.demo.storeweb;

import com.demo.storeweb.model.Product;
import com.demo.storeweb.model.User;
import com.demo.storeweb.model.UserOrder;
import com.demo.storeweb.repository.OrderRepository;
import com.demo.storeweb.repository.ProductRepository;
import com.demo.storeweb.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Datainit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository userOrderRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && productRepository.count() == 0 && userOrderRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setUsername("admin");
            defaultUser.setPassword("admin");
            defaultUser.setFavoriteCategoriesSet(Set.of("Books","Electronics","Clothing"));
            userRepository.save(defaultUser);

            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword("password1");
            user1.setFavoriteCategoriesSet(Set.of("Electronics","Books"));

            User user2 = new User();
            user2.setUsername("user2");
            user2.setPassword("password2");
            user2.setFavoriteCategoriesSet(Set.of("Movies","Music"));

            User user3 = new User();
            user3.setUsername("user3");
            user3.setPassword("password3");
            user3.setFavoriteCategoriesSet(Set.of("Clothing","Books"));

            User user4 = new User();
            user4.setUsername("user4");
            user4.setPassword("password4");
            user4.setFavoriteCategoriesSet(Set.of("Electronics","Movies"));

            User user5 = new User();
            user5.setUsername("user5");
            user5.setPassword("password5");
            user5.setFavoriteCategoriesSet(Set.of("Music","Clothing"));

            userRepository.saveAll(List.of(user1, user2, user3, user4, user5));

            // Mock Products
            Product product1 = new Product();
            product1.setName("Laptop");
            product1.setBrand("BrandA");
            product1.setCategory("Electronics");
            product1.setPrice("1000");
            product1.setDescription("A high performance laptop.");
            product1.setImageFileName("laptop.jpg");

            Product product2 = new Product();
            product2.setName("Book1");
            product2.setBrand("BrandB");
            product2.setCategory("Books");
            product2.setPrice("20");
            product2.setDescription("An interesting book.");
            product2.setImageFileName("book1.jpg");

            Product product3 = new Product();
            product3.setName("Smartphone");
            product3.setBrand("BrandC");
            product3.setCategory("Electronics");
            product3.setPrice("800");
            product3.setDescription("A latest model smartphone.");
            product3.setImageFileName("smartphone.jpg");

            Product product4 = new Product();
            product4.setName("Jeans");
            product4.setBrand("BrandD");
            product4.setCategory("Clothing");
            product4.setPrice("50");
            product4.setDescription("A pair of stylish jeans.");
            product4.setImageFileName("jeans.jpg");

            Product product5 = new Product();
            product5.setName("Guitar");
            product5.setBrand("BrandE");
            product5.setCategory("Music");
            product5.setPrice("150");
            product5.setDescription("An acoustic guitar.");
            product5.setImageFileName("guitar.jpg");

            Product product6 = new Product();
            product6.setName("Movie Ticket");
            product6.setBrand("BrandF");
            product6.setCategory("Movies");
            product6.setPrice("12");
            product6.setDescription("A ticket to the latest blockbuster movie.");
            product6.setImageFileName("movie_ticket.jpg");

            productRepository.saveAll(List.of(product1, product2, product3, product4, product5, product6));
            // Mock Orders
            UserOrder order1 = new UserOrder(user1, product1, "1234567890", "Address1", LocalDateTime.now(), 5);
            UserOrder order2 = new UserOrder(user2, product2, "0987654321", "Address2", LocalDateTime.now(), 4);
            UserOrder order3 = new UserOrder(user3, product3, "1231231234", "Address3", LocalDateTime.now(), 3);
            UserOrder order4 = new UserOrder(user4, product4, "3213214321", "Address4", LocalDateTime.now(), 4);
            UserOrder order5 = new UserOrder(user5, product5, "5555555555", "Address5", LocalDateTime.now(), 5);
            UserOrder order6 = new UserOrder(user1, product6, "1234567890", "Address1", LocalDateTime.now(), 3);
            UserOrder order7 = new UserOrder(user2, product1, "0987654321", "Address2", LocalDateTime.now(), 4);
            UserOrder order8 = new UserOrder(user3, product2, "1231231234", "Address3", LocalDateTime.now(), 2);
            UserOrder order9 = new UserOrder(user4, product3, "3213214321", "Address4", LocalDateTime.now(), 5);
            UserOrder order10 = new UserOrder(user5, product4, "5555555555", "Address5", LocalDateTime.now(), 4);

            userOrderRepository
                    .saveAll(List.of(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10));
        }

    }
}

package com.demo.storeweb.service;

import com.demo.storeweb.model.UserOrder;
import com.demo.storeweb.model.Product;
import com.demo.storeweb.model.User;
import com.demo.storeweb.repository.OrderRepository;
import com.demo.storeweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public UserOrder placeOrder(Integer productId, User user, String phone, String address) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        UserOrder order = new UserOrder(user, product, phone, address, LocalDateTime.now(), null);
        return orderRepository.save(order);
    }

    public void rateOrder(Long orderId, Integer rating) {
        UserOrder order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        order.setRating(rating);
        orderRepository.save(order);
    }
}

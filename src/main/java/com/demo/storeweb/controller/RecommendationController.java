package com.demo.storeweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.storeweb.model.Product;
import com.demo.storeweb.repository.ProductRepository;
import com.demo.storeweb.service.RecommendationService;

import java.util.List;
import java.util.Map;

@Controller
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private ProductRepository productRepository;

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    

    // /recommendations?user_id=(id nguoi dung)&num_recommendations=(so san pham muon goi y)
    @GetMapping("/recommendations")
    public String getRecommendations(
            @RequestParam("user_id") int userId,
            @RequestParam(value = "num_recommendations", defaultValue = "5") int numRecommendations, Model model) {
  
        // Fetch recommended product IDs from Flask server
        List<Integer> recommendedProductIds = restTemplate.getForObject(recommendationService.sendRequestToFlask(userId, numRecommendations), List.class);

        // Fetch product details from the database using the recommended product IDs
        List<Product> recommendedProducts = productRepository.findAllById(recommendedProductIds);

        model.addAttribute("recommendedProducts", recommendedProducts);

        return "recommend";
    }
}


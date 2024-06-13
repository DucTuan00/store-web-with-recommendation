package com.demo.storeweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.storeweb.service.RecommendationService;

import java.util.List;
import java.util.Map;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
    ///api/recommendations?user_id=(id nguoi dung)&num_recommendations=(so san pham muon goi y)
    @GetMapping("/api/recommendations")
    public List<Map<String, Object>> getRecommendations(
            @RequestParam("user_id") int userId,
            @RequestParam(value = "num_recommendations", defaultValue = "5") int numRecommendations) {
        return recommendationService.getRecommendations(userId, numRecommendations);
    }
}


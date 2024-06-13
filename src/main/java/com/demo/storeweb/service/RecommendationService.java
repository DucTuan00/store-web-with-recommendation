package com.demo.storeweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    private final RestTemplate restTemplate;

    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getRecommendations(int userId, int numRecommendations) {
        String url = UriComponentsBuilder.fromHttpUrl(flaskApiUrl)
                                         .pathSegment("recommend")
                                         .queryParam("user_id", userId)
                                         .queryParam("num_recommendations", numRecommendations)
                                         .toUriString();
        
        List<Map<String, Object>> recommendations = restTemplate.getForObject(url, List.class);
        return recommendations;
    }
}

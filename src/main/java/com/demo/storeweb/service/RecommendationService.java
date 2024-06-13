package com.demo.storeweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RecommendationService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    

    public String sendRequestToFlask(int userId, int numRecommendations) {
        String url = UriComponentsBuilder.fromHttpUrl(flaskApiUrl)
                                         .pathSegment("recommend")
                                         .queryParam("user_id", userId)
                                         .queryParam("num_recommendations", numRecommendations)
                                         .toUriString();
        return url;
    }
}

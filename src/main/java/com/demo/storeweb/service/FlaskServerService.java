package com.demo.storeweb.service;


import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.IOException;

@Service
public class FlaskServerService {
    private Process flaskProcess;

    @PostConstruct
    public void startFlaskServer() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "recommendation_system.py");
            processBuilder.redirectErrorStream(true);
            flaskProcess = processBuilder.start();
            System.out.println("Flask server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopFlaskServer() {
        if (flaskProcess != null) {
            flaskProcess.destroy();
            System.out.println("Flask server stopped.");
        }
    }
}


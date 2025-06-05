package com.project.Justick.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public String home() {
        return "Justick is running!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}


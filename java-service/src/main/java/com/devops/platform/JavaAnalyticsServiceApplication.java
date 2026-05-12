package com.devops.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JavaAnalyticsServiceApplication
 *
 * Entry point for the Spring Boot Analytics Microservice.
 * Provides REST APIs for reports, logs, and metrics consumed by the Node.js gateway.
 */
@SpringBootApplication
public class JavaAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaAnalyticsServiceApplication.class, args);
    }
}

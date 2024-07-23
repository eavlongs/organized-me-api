package com.organized_me.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String frontendUrl = Objects.requireNonNull(env.getProperty("frontend.url"), "Frontend URL must not be null");
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOriginPatterns(frontendUrl) // Use the frontend URL for allowed origin patterns
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials
    }
}
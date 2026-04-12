package com.Qauser.Quaser.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow your React dev server
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // Allow all standard headers (important for JWT/Authorization headers)
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));

        // Allow all common HTTP methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow cookies/auth headers to be sent
        config.setAllowCredentials(true);

        // Apply this to all endpoints in your Quaser app
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

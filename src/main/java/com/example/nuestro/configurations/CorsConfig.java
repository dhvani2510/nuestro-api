package com.example.nuestro.configurations;

import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.config.annotation.CorsRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Specify the allowed origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
                .allowedHeaders("Authorization", "Content-Type") // Include the "Authorization" header
                .allowCredentials(true);
    }
}

package com.task.swiftParser.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/swift-codes/**")
                .allowedOrigins("localhost")
                .allowedMethods("GET", "POST","DELETE")
                .allowedHeaders("Content-Type", "Authorization")
                .maxAge(3600);
    }

    @Bean
    public WebMvcConfigurer mvcConfigurer() {
        return new WebMvcConfigurer(){};
    }

}

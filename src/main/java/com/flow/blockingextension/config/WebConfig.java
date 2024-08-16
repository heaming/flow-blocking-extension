package com.flow.blockingextension.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("http://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com:8080")
                .allowedOrigins("https://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com:8080")
                .allowedOrigins("http://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com")
                .allowedOrigins("https://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com")
                .allowedOrigins("http://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com:80")
                .allowedOrigins("https://ec2-3-39-46-173.ap-northeast-2.compute.amazonaws.com:80")
                .allowCredentials(true);
    }
}

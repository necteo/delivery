package com.spring.delivery.config;

import com.spring.delivery.config.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AppProperties appProperties() {
        return new AppProperties();
    }
}

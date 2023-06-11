package com.spring.delivery.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Bean
    public AppProperties appProperties() {
        return new AppProperties();
    }
}

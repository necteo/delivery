package com.spring.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@Configuration
public class SecurityConfig extends WebSecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/store/**").hasRole("STORE_OWNER")
                .antMatchers("/order/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout().permitAll();
    }
}

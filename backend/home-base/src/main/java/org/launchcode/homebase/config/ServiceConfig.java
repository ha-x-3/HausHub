package org.launchcode.homebase.config;

import org.launchcode.homebase.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService() {
        return new UserService();
    }
}

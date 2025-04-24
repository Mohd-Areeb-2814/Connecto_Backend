package com.fb.friend_profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FriendProfileConfig {

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
package com.nttdata.msvc.product.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for creating a load-balanced Webclient instance.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates and configure a load-balanced WebClient.Builder
     * @return The configured WebClient.Builder instance
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }
}

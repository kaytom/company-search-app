package com.lexis.nexis.poc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${truProxyApi.baseUrl}")
    private String truProxyApiBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(truProxyApiBaseUrl).build();
    }
}

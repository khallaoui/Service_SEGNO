package com.marketplace.segno.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableRetry
@EnableCaching
public class RestTemplateConfig {

    @Value("${http.client.connection-timeout:5000}")
    private int connectionTimeout;

    @Value("${http.client.read-timeout:10000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient());
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofMilliseconds(connectionTimeout))
            .setResponseTimeout(Timeout.ofMilliseconds(readTimeout))
            .build();

        return HttpClients.custom()
            .setDefaultRequestConfig(config)
            .build();
    }
}
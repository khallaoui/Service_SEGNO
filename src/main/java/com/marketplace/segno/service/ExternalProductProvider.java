package com.marketplace.segno.service;

import com.marketplace.segno.dto.ProductDto;
import com.marketplace.segno.model.ApiResponse;
import com.marketplace.segno.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalProductProvider {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final HttpHeaders headers;
    
    @Autowired
    public ExternalProductProvider(RestTemplate restTemplate, 
                                 @Value("${external.api.base-url}") String baseUrl,
                                 @Value("${external.api.key:}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.headers = new HttpHeaders();
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + apiKey);
        }
        headers.set("Content-Type", "application/json");
    }
    
    @Cacheable("products")
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<ProductDto> getAllProducts() {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<ProductDto[]>> response = restTemplate.exchange(
            	    baseUrl + "/products",
            	    HttpMethod.GET,
            	    entity,
            	    new ParameterizedTypeReference<ApiResponse<ProductDto[]>>() {}
            	);

            
            ApiResponse<ProductDto[]> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                return Arrays.asList(apiResponse.getData());
            }
            
            throw new RuntimeException("Failed to fetch products: " + 
                (apiResponse != null ? apiResponse.getMessage() : "Unknown error"));
                
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products from external API", e);
        }
    }
    
    @Cacheable("categories")
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<String> getAllCategories() {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<CategoryDto[]>> response = restTemplate.exchange(
            	    baseUrl + "/categories",
            	    HttpMethod.GET,
            	    entity,
            	    new ParameterizedTypeReference<ApiResponse<CategoryDto[]>>() {}
            	);

            
            ApiResponse<CategoryDto[]> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                return Arrays.stream(apiResponse.getData())
                    .map(CategoryDto::getName)
                    .collect(Collectors.toList());
            }
            
            throw new RuntimeException("Failed to fetch categories: " + 
                (apiResponse != null ? apiResponse.getMessage() : "Unknown error"));
                
        } catch (Exception e) {
            throw new RuntimeException("Error fetching categories from external API", e);
        }
    }
    
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ProductDto getProductById(Long id) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<ProductDto>> response = restTemplate.exchange(
            	    baseUrl + "/products/" + id,
            	    HttpMethod.GET,
            	    entity,
            	    new ParameterizedTypeReference<ApiResponse<ProductDto>>() {}
            	);

            
            ApiResponse<ProductDto> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                return apiResponse.getData();
            }
            
            throw new RuntimeException("Failed to fetch product: " + 
                (apiResponse != null ? apiResponse.getMessage() : "Unknown error"));
                
        } catch (Exception e) {
            throw new RuntimeException("Error fetching product from external API", e);
        }
    }
    
    @Cacheable("products-by-category")
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<ProductDto> getProductsByCategory(String categoryId) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<ProductDto[]>> response = restTemplate.exchange(
            	    baseUrl + "/products?categoryId=" + categoryId,
            	    HttpMethod.GET,
            	    entity,
            	    new ParameterizedTypeReference<ApiResponse<ProductDto[]>>() {}
            	);

            
            ApiResponse<ProductDto[]> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                return Arrays.asList(apiResponse.getData());
            }
            
            throw new RuntimeException("Failed to fetch products by category: " + 
                (apiResponse != null ? apiResponse.getMessage() : "Unknown error"));
                
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products by category from external API", e);
        }
    }
    
    // NEW METHOD: Get search suggestions
    @Cacheable("search-suggestions")
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<String> getSearchSuggestions(String query, int limit) {
        try {
            // Try to get suggestions from external API if it supports this endpoint
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // If the external API has a suggestions endpoint, use it
            // Otherwise, fallback to generating suggestions from product names
            try {
            	ResponseEntity<ApiResponse<String[]>> response = restTemplate.exchange(
            		    baseUrl + "/suggestions?query=" + query + "&limit=" + limit,
            		    HttpMethod.GET,
            		    entity,
            		    new ParameterizedTypeReference<ApiResponse<String[]>>() {}
            		);

                
                ApiResponse<String[]> apiResponse = response.getBody();
                if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                    return Arrays.asList(apiResponse.getData());
                }
            } catch (Exception e) {
                // If suggestions endpoint doesn't exist, fallback to generating from products
                // This is handled in the SearchService, so return empty list
            }
            
            // Return empty list to indicate no external suggestions available
            // SearchService will handle the fallback logic
            return List.of();
            
        } catch (Exception e) {
            // Return empty list on error, SearchService will handle fallback
            return List.of();
        }
    }
}
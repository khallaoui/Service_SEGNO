package com.marketplace.segno.controller;


import com.marketplace.segno.dto.*;
import com.marketplace.segno.service.SearchCriteria;
import com.marketplace.segno.service.SearchService;
import com.marketplace.segno.service.SearchHistoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "*")
public class SearchController {
    
    private final SearchService searchService;
    private final SearchHistoryService searchHistoryService;
    
    @Autowired
    public SearchController(SearchService searchService, SearchHistoryService searchHistoryService) {
        this.searchService = searchService;
        this.searchHistoryService = searchHistoryService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @PositiveOrZero Double minPrice,
            @RequestParam(required = false) @PositiveOrZero Double maxPrice,
            @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("5.0") Double minRating,
            @RequestParam(required = false, defaultValue = "relevance") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(required = false) String userId) {
        
        SearchCriteria criteria = new SearchCriteria();
        criteria.setQuery(q);
        criteria.setCategory(category);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setMinRating(minRating);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);
        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setUserId(userId);
        
        SearchResponse response = searchService.searchProducts(criteria);
        
        // Enregistrer dans l'historique si un utilisateur est connecté et une recherche est effectuée
        if (userId != null && q != null && !q.trim().isEmpty()) {
            searchHistoryService.saveSearchToHistory(userId, q, response.getTotalResults());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search/suggestions")
    public ResponseEntity<Map<String, List<String>>> getSearchSuggestions(
            @RequestParam @NotBlank @Size(min = 2) String q,
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(10) Integer limit) {
        
        List<String> suggestions = searchService.getSearchSuggestions(q, limit);
        Map<String, List<String>> response = new HashMap<>();
        response.put("suggestions", suggestions);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search/popular")
    public ResponseEntity<Map<String, List<ProductDto>>> getPopularSearches(
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(20) Integer limit) {
        
        List<ProductDto> popularProducts = searchService.getPopularProducts(limit);
        Map<String, List<ProductDto>> response = new HashMap<>();
        response.put("popularProducts", popularProducts);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search/categories")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getCategories() {
        List<String> categories = searchService.getAllCategories();
        
        List<Map<String, Object>> categoryList = categories.stream()
            .map(category -> {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("id", category.toLowerCase().replace(" ", "_"));
                categoryMap.put("name", category);
                categoryMap.put("productCount", 0); // À implémenter selon les besoins
                return categoryMap;
            })
            .toList();
        
        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("categories", categoryList);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search/history/{userId}")
    public ResponseEntity<Map<String, List<SearchHistoryDto>>> getUserSearchHistory(
            @PathVariable @NotBlank String userId,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(50) Integer limit) {
        
        List<SearchHistoryDto> history = searchHistoryService.getUserSearchHistory(userId, limit);
        Map<String, List<SearchHistoryDto>> response = new HashMap<>();
        response.put("history", history);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/search/history/{userId}")
    public ResponseEntity<Void> saveSearchToHistory(
            @PathVariable @NotBlank String userId,
            @RequestBody @Valid SearchHistoryDto searchHistoryEntry) {
        
        searchHistoryService.saveSearchToHistory(userId, searchHistoryEntry.getQuery(), 
                                               searchHistoryEntry.getResultCount());
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("/search/history/{userId}")
    public ResponseEntity<Void> clearSearchHistory(@PathVariable @NotBlank String userId) {
        searchHistoryService.clearSearchHistory(userId);
        return ResponseEntity.noContent().build();
    }
}

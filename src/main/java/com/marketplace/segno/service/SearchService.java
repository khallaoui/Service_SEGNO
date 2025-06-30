package com.marketplace.segno.service;


import com.marketplace.segno.dto.*;
import com.marketplace.segno.model.Product;
import com.marketplace.segno.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SearchService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Autowired
    public SearchService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    
    public SearchResponse searchProducts(SearchCriteria criteria) {
        long startTime = System.currentTimeMillis();
        
        Pageable pageable = createPageable(criteria);
        Page<Product> productPage = productRepository.searchProducts(
            criteria.getQuery(),
            criteria.getCategory(),
            criteria.getMinPrice(),
            criteria.getMaxPrice(),
            criteria.getMinRating(),
            pageable
        );
        
        List<ProductDto> productDtos = productPage.getContent().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
        
        PaginationDto pagination = createPaginationDto(productPage);
        AppliedFiltersDto filters = createAppliedFiltersDto(criteria);
        
        long endTime = System.currentTimeMillis();
        double searchTime = endTime - startTime;
        
        return new SearchResponse(productDtos, pagination, (int) productPage.getTotalElements(), searchTime);
    }
    
    @Cacheable("suggestions")
    public List<String> getSearchSuggestions(String query, int limit) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }
        return productRepository.findSuggestions(query.trim(), limit);
    }
    
    @Cacheable("categories")
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
    
    @Cacheable("popular-products")
    public List<ProductDto> getPopularProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Product> popularProducts = productRepository.findPopularProducts(pageable);
        return popularProducts.getContent().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }
    
    private Pageable createPageable(SearchCriteria criteria) {
        Sort sort = createSort(criteria.getSortBy(), criteria.getSortOrder());
        return PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
    }
    
    private Sort createSort(String sortBy, String sortOrder) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        
        return switch (sortBy != null ? sortBy.toLowerCase() : "relevance") {
            case "name" -> Sort.by(direction, "name");
            case "price" -> Sort.by(direction, "price");
            case "rating" -> Sort.by(direction, "rating");
            case "popularity" -> Sort.by(direction, "reviewCount");
            default -> Sort.by(Sort.Direction.DESC, "reviewCount", "rating");
        };
    }
    
    private PaginationDto createPaginationDto(Page<Product> page) {
        return new PaginationDto(
            page.getNumber(),
            page.getTotalPages(),
            page.getSize(),
            (int) page.getTotalElements(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
    
    private AppliedFiltersDto createAppliedFiltersDto(SearchCriteria criteria) {
        AppliedFiltersDto filters = new AppliedFiltersDto();
        filters.setQuery(criteria.getQuery());
        filters.setCategory(criteria.getCategory());
        filters.setMinRating(criteria.getMinRating());
        filters.setSortBy(criteria.getSortBy());
        filters.setSortOrder(criteria.getSortOrder());
        
        if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
            filters.setPriceRange(new PriceRangeDto(criteria.getMinPrice(), criteria.getMaxPrice()));
        }
        
        return filters;
    }
}

package com.marketplace.segno.dto;

import java.util.List;

public class SearchResponse {
    private List<ProductDto> products;
    private PaginationDto pagination;
    private AppliedFiltersDto filters;
    private Integer totalResults;
    private Double searchTime;
    
    // Default constructor
    public SearchResponse() {}
    
    // Existing constructor without filters
    public SearchResponse(List<ProductDto> products, PaginationDto pagination, 
                         Integer totalResults, Double searchTime) {
        this.products = products;
        this.pagination = pagination;
        this.totalResults = totalResults;
        this.searchTime = searchTime;
    }

    // New constructor with filters
    public SearchResponse(List<ProductDto> products, PaginationDto pagination, 
                          AppliedFiltersDto filters, Integer totalResults, Double searchTime) {
        this.products = products;
        this.pagination = pagination;
        this.filters = filters;
        this.totalResults = totalResults;
        this.searchTime = searchTime;
    }
    
    // Getters and Setters
    public List<ProductDto> getProducts() { return products; }
    public void setProducts(List<ProductDto> products) { this.products = products; }
    
    public PaginationDto getPagination() { return pagination; }
    public void setPagination(PaginationDto pagination) { this.pagination = pagination; }
    
    public AppliedFiltersDto getFilters() { return filters; }
    public void setFilters(AppliedFiltersDto filters) { this.filters = filters; }
    
    public Integer getTotalResults() { return totalResults; }
    public void setTotalResults(Integer totalResults) { this.totalResults = totalResults; }
    
    public Double getSearchTime() { return searchTime; }
    public void setSearchTime(Double searchTime) { this.searchTime = searchTime; }
}

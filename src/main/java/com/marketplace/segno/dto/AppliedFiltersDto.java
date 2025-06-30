package com.marketplace.segno.dto;


public class AppliedFiltersDto {
    private String query;
    private String category;
    private PriceRangeDto priceRange;
    private Double minRating;
    private String sortBy;
    private String sortOrder;
    
    // Constructors, getters, setters...
    public AppliedFiltersDto() {}
    
    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public PriceRangeDto getPriceRange() { return priceRange; }
    public void setPriceRange(PriceRangeDto priceRange) { this.priceRange = priceRange; }
    
    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
}
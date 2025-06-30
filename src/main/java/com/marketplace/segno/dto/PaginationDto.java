package com.marketplace.segno.dto;


public class PaginationDto {
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private Integer totalElements;
    private Boolean hasNext;
    private Boolean hasPrevious;
    
    // Constructors, getters, setters...
    public PaginationDto() {}
    
    public PaginationDto(Integer currentPage, Integer totalPages, Integer pageSize, 
                        Integer totalElements, Boolean hasNext, Boolean hasPrevious) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
    
    // Getters and Setters
    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    
    public Integer getTotalElements() { return totalElements; }
    public void setTotalElements(Integer totalElements) { this.totalElements = totalElements; }
    
    public Boolean getHasNext() { return hasNext; }
    public void setHasNext(Boolean hasNext) { this.hasNext = hasNext; }
    
    public Boolean getHasPrevious() { return hasPrevious; }
    public void setHasPrevious(Boolean hasPrevious) { this.hasPrevious = hasPrevious; }
}


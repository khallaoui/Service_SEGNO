package com.marketplace.segno.dto;


import java.time.LocalDateTime;

public class SearchHistoryDto {
    private String query;
    private LocalDateTime timestamp;
    private Integer resultCount;
    
    public SearchHistoryDto() {}
    
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Integer getResultCount() { return resultCount; }
    public void setResultCount(Integer resultCount) { this.resultCount = resultCount; }
}
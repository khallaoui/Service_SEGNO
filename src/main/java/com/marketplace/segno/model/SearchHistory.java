package com.marketplace.segno.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history", indexes = {
    @Index(name = "idx_search_history_user", columnList = "userId"),
    @Index(name = "idx_search_history_timestamp", columnList = "timestamp")
})
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String query;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private Integer resultCount;
    
    // Constructors, getters, setters
    public SearchHistory() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Integer getResultCount() { return resultCount; }
    public void setResultCount(Integer resultCount) { this.resultCount = resultCount; }
}
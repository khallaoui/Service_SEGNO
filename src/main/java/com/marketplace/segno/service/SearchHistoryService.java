package com.marketplace.segno.service;


import com.marketplace.segno.dto.SearchHistoryDto;
import com.marketplace.segno.model.SearchHistory;
import com.marketplace.segno.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchHistoryService {
    
    private final SearchHistoryRepository searchHistoryRepository;
    
    @Autowired
    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }
    
    public List<SearchHistoryDto> getUserSearchHistory(String userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<SearchHistory> history = searchHistoryRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        
        return history.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public void saveSearchToHistory(String userId, String query, int resultCount) {
        SearchHistory history = new SearchHistory();
        history.setUserId(userId);
        history.setQuery(query);
        history.setResultCount(resultCount);
        history.setTimestamp(LocalDateTime.now());
        
        searchHistoryRepository.save(history);
    }
    
    public void clearSearchHistory(String userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }
    
    private SearchHistoryDto toDto(SearchHistory entity) {
        SearchHistoryDto dto = new SearchHistoryDto();
        dto.setQuery(entity.getQuery());
        dto.setTimestamp(entity.getTimestamp());
        dto.setResultCount(entity.getResultCount());
        return dto;
    }
}

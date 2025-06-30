package com.marketplace.segno.repository;


import com.marketplace.segno.model.SearchHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderByTimestampDesc(String userId, Pageable pageable);
    void deleteByUserId(String userId);
}
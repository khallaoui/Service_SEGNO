package com.marketplace.segno.service;

import com.marketplace.segno.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SearchService {

    private final ExternalProductProvider externalProductProvider;
    private final SearchHistoryService searchHistoryService;

    @Autowired
    public SearchService(ExternalProductProvider externalProductProvider,
                         SearchHistoryService searchHistoryService) {
        this.externalProductProvider = externalProductProvider;
        this.searchHistoryService = searchHistoryService;
    }

    @Transactional(readOnly = false)
    public SearchResponse searchProducts(SearchCriteria criteria) {
        long startTime = System.currentTimeMillis();

        // 1. Fetch all products from external service
        List<ProductDto> allProducts = externalProductProvider.getAllProducts();
        if (allProducts == null) {
            allProducts = List.of();
        }

        // 2. Apply filters in memory
        List<ProductDto> filteredProducts = allProducts.stream()
                .filter(product -> matchesTextQuery(product, criteria.getQuery()))
                .filter(product -> matchesCategory(product, criteria.getCategory()))
                .filter(product -> matchesPriceRange(product, criteria.getMinPrice(), criteria.getMaxPrice()))
                .filter(product -> matchesRating(product, criteria.getMinRating()))
                .sorted(createComparator(criteria.getSortBy(), criteria.getSortOrder()))
                .collect(Collectors.toList());

     // 3. Apply pagination
        int totalElements = filteredProducts.size();

        // Assume page and size have defaults if invalid (negative or zero for size)
        int page = criteria.getPage() >= 0 ? criteria.getPage() : 0;
        int size = criteria.getSize() > 0 ? criteria.getSize() : 10;

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);


        List<ProductDto> pageContent = fromIndex >= totalElements ?
                List.of() : filteredProducts.subList(fromIndex, toIndex);

        // 4. Create pagination info
        PaginationDto pagination = createPaginationDto(page, size, totalElements);
        AppliedFiltersDto filters = createAppliedFiltersDto(criteria);

        long endTime = System.currentTimeMillis();
        double searchTime = (endTime - startTime) / 1000.0;  // convert ms to seconds

        // 5. Save search to history if user is provided and query is not blank
        if (criteria.getUserId() != null && criteria.getQuery() != null && !criteria.getQuery().trim().isEmpty()) {
            searchHistoryService.saveSearchToHistory(criteria.getUserId(), criteria.getQuery(), totalElements);
        }

        return new SearchResponse(pageContent, pagination, filters, totalElements, searchTime);}

    @Cacheable("suggestions")
    public List<String> getSearchSuggestions(String query, int limit) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        // Try to get suggestions from external service first
        List<String> externalSuggestions = externalProductProvider.getSearchSuggestions(query.trim(), limit);

        // If external service doesn't provide suggestions, generate from product names
        if (externalSuggestions == null || externalSuggestions.isEmpty()) {
            return externalProductProvider.getAllProducts().stream()
                    .map(ProductDto::getName)
                    .filter(name -> name != null && name.toLowerCase().contains(query.toLowerCase()))
                    .distinct()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return externalSuggestions;
    }

    @Cacheable("categories")
    public List<String> getAllCategories() {
        List<String> categories = externalProductProvider.getAllCategories();
        return categories != null ? categories : List.of();
    }

    @Cacheable("popular-products")
    public List<ProductDto> getPopularProducts(int limit) {
        List<ProductDto> products = externalProductProvider.getAllProducts();
        if (products == null) {
            return List.of();
        }

        return products.stream()
                .sorted(Comparator
                        .comparing((ProductDto p) -> p.getReviewCount() != null ? p.getReviewCount() : 0, Comparator.reverseOrder())
                        .thenComparing((ProductDto p) -> p.getRating() != null ? p.getRating() : 0.0, Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean matchesTextQuery(ProductDto product, String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }

        String lowerQuery = query.toLowerCase();

        boolean nameMatch = product.getName() != null && product.getName().toLowerCase().contains(lowerQuery);
        boolean descriptionMatch = product.getDescription() != null && product.getDescription().toLowerCase().contains(lowerQuery);
        boolean brandMatch = product.getBrand() != null && product.getBrand().toLowerCase().contains(lowerQuery);
        boolean tagsMatch = product.getTags() != null && product.getTags().stream()
                .filter(tag -> tag != null)
                .anyMatch(tag -> tag.toLowerCase().contains(lowerQuery));

        return nameMatch || descriptionMatch || brandMatch || tagsMatch;
    }

    
    private boolean matchesCategory(ProductDto product, String category) {
        if (category == null || category.trim().isEmpty()) {
            return true;
        }
        if (product.getCategory() == null) {
            return false;
        }

        String productCat = product.getCategory().trim();
        String searchCat = category.trim();
        System.out.println("Comparing category: [" + productCat + "] with [" + searchCat + "]");

        return productCat.equalsIgnoreCase(searchCat);
    }

    
    

    private boolean matchesPriceRange(ProductDto product, Double minPrice, Double maxPrice) {
        if (product.getPrice() == null) return false;
        if (minPrice != null && product.getPrice() < minPrice) return false;
        if (maxPrice != null && product.getPrice() > maxPrice) return false;
        return true;
    }

    private boolean matchesRating(ProductDto product, Double minRating) {
        if (minRating == null) {
            return true;
        }
        return product.getRating() != null && product.getRating() >= minRating;
    }

    private Comparator<ProductDto> createComparator(String sortBy, String sortOrder) {
        boolean isAscending = "asc".equalsIgnoreCase(sortOrder);

        Comparator<ProductDto> comparator = switch (sortBy != null ? sortBy.toLowerCase() : "relevance") {
            case "name" -> Comparator.comparing(
                    ProductDto::getName, Comparator.nullsLast(String::compareToIgnoreCase));
            case "price" -> Comparator.comparing(
                    ProductDto::getPrice, Comparator.nullsLast(Double::compareTo));
            case "rating" -> Comparator.comparing(
                    ProductDto::getRating, Comparator.nullsLast(Double::compareTo));
            case "popularity" -> Comparator.comparing(
                    ProductDto::getReviewCount, Comparator.nullsLast(Integer::compareTo));
            default -> Comparator
                    .comparing(ProductDto::getReviewCount, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(ProductDto::getRating, Comparator.nullsLast(Double::compareTo));
        };

        return isAscending ? comparator : comparator.reversed();
    }

    private PaginationDto createPaginationDto(int page, int size, int totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean hasNext = page < totalPages - 1;
        boolean hasPrevious = page > 0;

        return new PaginationDto(page, totalPages, size, totalElements, hasNext, hasPrevious);
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

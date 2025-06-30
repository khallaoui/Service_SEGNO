package com.marketplace.segno.service;


import com.marketplace.segno.dto.ProductDto;
import com.marketplace.segno.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setImageUrl(product.getImageUrl());
        dto.setAvailability(product.getAvailability() != null ? 
            product.getAvailability().name() : null);
        dto.setBrand(product.getBrand());
        dto.setTags(product.getTags());
        
        return dto;
    }
    
    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setRating(dto.getRating());
        product.setReviewCount(dto.getReviewCount());
        product.setImageUrl(dto.getImageUrl());
        product.setBrand(dto.getBrand());
        product.setTags(dto.getTags());
        
        return product;
    }
}
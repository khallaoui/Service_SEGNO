package com.marketplace.segno.dto;

import java.util.List;
import com.marketplace.segno.model.Product.Availability;


public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String category;       // category name or ID string
    private Double price;          // current price (after discount maybe)
    private Double originalPrice;  // base/original price before discount
    private Double rating;         // average rating 0-5
    private Integer reviewCount;
    private String imageUrl;       // main image url
    private Availability availability;
    private String brand;
    private List<String> tags;

    public ProductDto() {}

    // Getters and setters (generated for all fields)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Availability getAvailability() { return availability; }
    public void setAvailability(Availability availability) { this.availability = availability; }
    
}

package com.marketplace.segno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_category", columnList = "category"),
    @Index(name = "idx_product_price", columnList = "price"),
    @Index(name = "idx_product_rating", columnList = "rating")
})
public class Product {

    @Id
    private String id;  // Could be UUID string

    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank
    @Size(max = 100)
    private String category;  // Ideally this is a relation to Category entity, but kept as string here for simplicity

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double price;

    @PositiveOrZero
    private Double originalPrice;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;

    @Min(0)
    private Integer reviewCount = 0;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Availability availability = Availability.IN_STOCK;

    @Size(max = 100)
    private String brand;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product() {}

    // All getters and setters...

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

//    public Availability getAvailability() { return availability; }
//    public void setAvailability(Availability availability) { this.availability = availability; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Enum for availability inside Product
    public enum Availability {
        IN_STOCK,
        OUT_OF_STOCK,
        DISCONTINUED
    }
    public Availability getAvailability() { return availability; }
    public void setAvailability(Availability availability) { this.availability = availability; }

}

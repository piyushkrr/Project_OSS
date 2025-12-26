package com.oss.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String category;

    private Integer stockQuantity;

    @Lob
    @JsonIgnore
    @Column(name = "main_image", length = 100000000)
    private byte[] mainImage;

    private String imageType;

    private String mainImageUrl;

    public String getMainImageUrl() {
        if (this.id != null && this.mainImage != null) {
            return "http://localhost:8181/api/products/" + this.id + "/image";
        }
        return mainImageUrl;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // Unidirectional join for simplicity
    private List<Image> images;

    private Double rating;
    private Boolean isPopular;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (rating == null)
            rating = 0.0;
        if (isPopular == null)
            isPopular = false;
    }
}

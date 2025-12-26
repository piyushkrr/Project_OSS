package com.oss.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String mainImageUrl;
    private Double rating;
    private Boolean isPopular;
    private LocalDateTime createdAt;
}

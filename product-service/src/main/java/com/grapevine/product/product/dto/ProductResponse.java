package com.grapevine.product.product.dto;

import com.grapevine.product.product.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private ProductCategory category;

    private Integer volume;

    private Integer year;

    private String imageUrl;

    private Boolean active;

}
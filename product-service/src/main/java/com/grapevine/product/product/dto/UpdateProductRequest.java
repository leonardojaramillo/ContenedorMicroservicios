package com.grapevine.product.product.dto;

import com.grapevine.product.product.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private ProductCategory category;
    private Integer volume;
    private Integer year;
    private String imageUrl;
}
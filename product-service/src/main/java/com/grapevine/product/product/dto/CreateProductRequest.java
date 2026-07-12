package com.grapevine.product.product.dto;

import com.grapevine.product.product.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private ProductCategory category;
    private Integer volume;
    private Integer year;
    private String imageUrl;

}
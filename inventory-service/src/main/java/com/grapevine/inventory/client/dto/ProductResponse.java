package com.grapevine.inventory.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer volume;
    private Integer year;
    private String imageUrl;
    private Boolean active;
}
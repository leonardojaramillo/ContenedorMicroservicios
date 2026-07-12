package com.grapevine.reporting.client.dto;

import lombok.Data;

@Data
public class WarehouseStockResponse {
    private Long productId;
    private String productName;
    private Integer stock;
}
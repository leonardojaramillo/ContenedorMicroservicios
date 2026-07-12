package com.grapevine.sales.client.dto;

import lombok.Data;

@Data
public class WarehouseStockResponse {
    private Long warehouseStockId;
    private Long warehouseId;
    private String warehouseName;
    private Long productId;
    private String productName;
    private String productCategory;
    private Integer stock;
}
package com.grapevine.purchase.client.dto;

import lombok.Data;

@Data
public class AdjustStockRequest {
    private Long productId;
    private Long warehouseId;
    private Integer newStock;
    private String reason;
}
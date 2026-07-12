package com.grapevine.inventory.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustStockRequest {
    private Long productId;
    private Long warehouseId;
    private Integer newStock;
    private String reason;
}
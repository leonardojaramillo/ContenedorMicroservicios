package com.grapevine.inventory.inventory.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryAdjustmentResponse {
    private Long id;
    private String productName;
    private String warehouseName;
    private Integer previousStock;
    private Integer newStock;
    private String reason;
    private LocalDateTime createdAt;
}
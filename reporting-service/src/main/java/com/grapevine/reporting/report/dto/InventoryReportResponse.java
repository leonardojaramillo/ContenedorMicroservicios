package com.grapevine.reporting.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class InventoryReportResponse implements Serializable {
    private Long totalProducts;
    private Long lowStockProducts;
}
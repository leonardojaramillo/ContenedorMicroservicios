package com.grapevine.reporting.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogisticaDashboardResponse {
    private Long totalProducts;
    private Long lowStockProducts;
}
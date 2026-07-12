package com.grapevine.reporting.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
public class DashboardResponse implements Serializable {
    private Long totalProducts;
    private Long totalOrders;
    private Long totalPurchases;
    private BigDecimal totalSales;
    private BigDecimal todaySales;
    private Long lowStockProducts;
}
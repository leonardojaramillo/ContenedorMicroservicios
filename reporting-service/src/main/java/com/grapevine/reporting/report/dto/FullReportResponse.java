package com.grapevine.reporting.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class FullReportResponse implements Serializable {
    private Long totalOrders;
    private BigDecimal totalSales;
    private Long openedRegisters;
    private BigDecimal totalCash;
    private Long totalProducts;
    private Long lowStockProducts;
    private Long totalPurchases;
    private BigDecimal totalSpent;
    private List<MonthlyDataPoint> monthly;
}
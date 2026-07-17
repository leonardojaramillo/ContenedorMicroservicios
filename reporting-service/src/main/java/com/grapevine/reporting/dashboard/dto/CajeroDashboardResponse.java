package com.grapevine.reporting.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CajeroDashboardResponse {
    private Long totalOrders;
    private BigDecimal totalSales;
    private BigDecimal todaySales;
}
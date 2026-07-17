package com.grapevine.reporting.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class VendedorDashboardResponse {
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal todaySales;
}
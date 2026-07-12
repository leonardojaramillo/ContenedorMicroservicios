package com.grapevine.sales.order.dto;

import com.grapevine.sales.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private String customerName;
    private String customerDocument;
    private String warehouseName;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String cancelReason;
    private LocalDateTime cancelledAt;
    private List<OrderDetailResponse> details;
}
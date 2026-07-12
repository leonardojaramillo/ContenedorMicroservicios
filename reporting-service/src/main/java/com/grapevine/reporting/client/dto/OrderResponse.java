package com.grapevine.reporting.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
}
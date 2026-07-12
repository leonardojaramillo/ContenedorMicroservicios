package com.grapevine.reporting.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashRegisterResponse {
    private Long id;
    private String status;
    private BigDecimal closingAmount;
    private LocalDateTime openedAt;
}
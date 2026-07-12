package com.grapevine.finance.cash.dto;

import com.grapevine.finance.cash.MovementType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCashMovementRequest {

    private MovementType type;
    private String description;
    private BigDecimal amount;
    private String receiptUrl;
}
package com.grapevine.reporting.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
public class MonthlyDataPoint implements Serializable {
    private String month;
    private BigDecimal sales;
    private BigDecimal purchases;
}
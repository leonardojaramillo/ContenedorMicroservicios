package com.grapevine.reporting.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
public class CashReportResponse implements Serializable {
    private Long openedRegisters;
    private BigDecimal totalCash;
}
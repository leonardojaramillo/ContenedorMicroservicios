package com.grapevine.reporting.report;

import com.grapevine.reporting.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public SalesReportResponse sales() {
        return reportService.getSalesReport();
    }

    @GetMapping("/inventory")
    public InventoryReportResponse inventory() {
        return reportService.getInventoryReport();
    }

    @GetMapping("/cash")
    public CashReportResponse cash() {
        return reportService.getCashReport();
    }

    @GetMapping("/purchases")
    public PurchaseReportResponse purchases() {
        return reportService.getPurchaseReport();
    }

    @GetMapping("/full")
    public FullReportResponse full(@RequestParam(required = false) String start,
                                   @RequestParam(required = false) String end) {
        LocalDate s = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate e = (end != null && !end.isBlank()) ? LocalDate.parse(end) : null;
        return reportService.getFullReport(s, e);
    }
}
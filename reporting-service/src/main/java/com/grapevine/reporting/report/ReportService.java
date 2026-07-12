package com.grapevine.reporting.report;

import com.grapevine.reporting.client.*;
import com.grapevine.reporting.client.dto.*;
import com.grapevine.reporting.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SalesClient     salesClient;
    private final PurchaseClient  purchaseClient;
    private final FinanceClient   financeClient;
    private final ProductClient   productClient;
    private final InventoryClient inventoryClient;

    @Cacheable("report-sales")
    public SalesReportResponse getSalesReport() {
        List<OrderResponse> orders = salesClient.findAll();
        return SalesReportResponse.builder()
                .totalOrders((long) orders.size())
                .totalSales(sumPaidOrders(orders))
                .build();
    }

    @Cacheable("report-inventory")
    public InventoryReportResponse getInventoryReport() {
        Long totalProducts = (long) productClient.findAll().size();
        Long lowStock = countLowStockProducts();

        return InventoryReportResponse.builder()
                .totalProducts(totalProducts)
                .lowStockProducts(lowStock)
                .build();
    }

    @Cacheable("report-cash")
    public CashReportResponse getCashReport() {
        List<CashRegisterResponse> registers = financeClient.getAllRegisters();

        BigDecimal totalCash = registers.stream()
                .map(r -> r.getClosingAmount() == null ? BigDecimal.ZERO : r.getClosingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CashReportResponse.builder()
                .openedRegisters((long) registers.size())
                .totalCash(totalCash)
                .build();
    }

    @Cacheable("report-purchases")
    public PurchaseReportResponse getPurchaseReport() {
        List<PurchaseResponse> purchases = purchaseClient.findAll();
        BigDecimal totalSpent = sumPaidPurchases(purchases);

        return PurchaseReportResponse.builder()
                .totalPurchases((long) purchases.size())
                .totalSpent(totalSpent)
                .build();
    }

    @Cacheable(value = "report-full", key = "#start + '_' + #end")
    public FullReportResponse getFullReport(LocalDate start, LocalDate end) {

        List<OrderResponse>    allOrders    = salesClient.findAll();
        List<PurchaseResponse> allPurchases = purchaseClient.findAll();

        if (start != null && end != null) {
            return buildRangeReport(start, end, allOrders, allPurchases);
        }

        return buildDefaultReport(allOrders, allPurchases);
    }

    private FullReportResponse buildDefaultReport(List<OrderResponse> orders, List<PurchaseResponse> purchases) {

        List<MonthlyDataPoint> monthly = buildMonthlyData(orders, purchases);

        List<CashRegisterResponse> registers = financeClient.getAllRegisters();
        BigDecimal totalCash = registers.stream()
                .map(r -> r.getClosingAmount() == null ? BigDecimal.ZERO : r.getClosingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return FullReportResponse.builder()
                .totalOrders((long) orders.size())
                .totalSales(sumPaidOrders(orders))
                .openedRegisters((long) registers.size())
                .totalCash(totalCash)
                .totalProducts((long) productClient.findAll().size())
                .lowStockProducts(countLowStockProducts())
                .totalPurchases((long) purchases.size())
                .totalSpent(sumPaidPurchases(purchases))
                .monthly(monthly)
                .build();
    }

    private FullReportResponse buildRangeReport(LocalDate start, LocalDate end,
                                                List<OrderResponse> orders, List<PurchaseResponse> purchases) {

        LocalDateTime rangeStart = start.atStartOfDay();
        LocalDateTime rangeEnd   = end.atTime(23, 59, 59);

        List<OrderResponse> filteredOrders = orders.stream()
                .filter(o -> o.getCreatedAt() != null
                        && !o.getCreatedAt().isBefore(rangeStart)
                        && !o.getCreatedAt().isAfter(rangeEnd))
                .toList();

        List<PurchaseResponse> filteredPurchases = purchases.stream()
                .filter(p -> p.getCreatedAt() != null
                        && !p.getCreatedAt().isBefore(rangeStart)
                        && !p.getCreatedAt().isAfter(rangeEnd))
                .toList();

        List<CashRegisterResponse> filteredCash = financeClient.getAllRegisters().stream()
                .filter(c -> c.getOpenedAt() != null
                        && !c.getOpenedAt().isBefore(rangeStart)
                        && !c.getOpenedAt().isAfter(rangeEnd))
                .toList();

        BigDecimal totalCash = filteredCash.stream()
                .map(c -> c.getClosingAmount() == null ? BigDecimal.ZERO : c.getClosingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MonthlyDataPoint> daily = buildDailyData(start, end, filteredOrders, filteredPurchases);

        return FullReportResponse.builder()
                .totalOrders((long) filteredOrders.size())
                .totalSales(sumPaidOrders(filteredOrders))
                .openedRegisters((long) filteredCash.size())
                .totalCash(totalCash)
                .totalProducts((long) productClient.findAll().size())
                .lowStockProducts(countLowStockProducts())
                .totalPurchases((long) filteredPurchases.size())
                .totalSpent(sumPaidPurchases(filteredPurchases))
                .monthly(daily)
                .build();
    }

    private Long countLowStockProducts() {
        Map<Long, Integer> totalByProduct = inventoryClient.getAllStock().stream()
                .collect(Collectors.groupingBy(
                        WarehouseStockResponse::getProductId,
                        Collectors.summingInt(WarehouseStockResponse::getStock)
                ));

        return totalByProduct.values().stream().filter(s -> s <= 5).count();
    }

    private BigDecimal sumPaidOrders(List<OrderResponse> orders) {
        return orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .map(OrderResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumPaidPurchases(List<PurchaseResponse> purchases) {
        return purchases.stream()
                .filter(p -> "PAID".equals(p.getStatus()))
                .map(PurchaseResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<MonthlyDataPoint> buildMonthlyData(List<OrderResponse> orders, List<PurchaseResponse> purchases) {
        List<MonthlyDataPoint> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 5; i >= 0; i--) {
            LocalDateTime month = now.minusMonths(i);
            int m = month.getMonthValue();
            int y = month.getYear();

            BigDecimal sales = orders.stream()
                    .filter(o -> "PAID".equals(o.getStatus())
                            && o.getCreatedAt() != null
                            && o.getCreatedAt().getMonthValue() == m
                            && o.getCreatedAt().getYear() == y)
                    .map(OrderResponse::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal spent = purchases.stream()
                    .filter(p -> "PAID".equals(p.getStatus())
                            && p.getCreatedAt() != null
                            && p.getCreatedAt().getMonthValue() == m
                            && p.getCreatedAt().getYear() == y)
                    .map(PurchaseResponse::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String label = month.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "PE"));

            result.add(MonthlyDataPoint.builder().month(label).sales(sales).purchases(spent).build());
        }

        return result;
    }

    private List<MonthlyDataPoint> buildDailyData(LocalDate start, LocalDate end,
                                                  List<OrderResponse> orders, List<PurchaseResponse> purchases) {
        List<MonthlyDataPoint> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate current = start;
        while (!current.isAfter(end)) {
            LocalDate day = current;

            BigDecimal sales = orders.stream()
                    .filter(o -> "PAID".equals(o.getStatus())
                            && o.getCreatedAt() != null
                            && o.getCreatedAt().toLocalDate().isEqual(day))
                    .map(OrderResponse::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal spent = purchases.stream()
                    .filter(p -> "PAID".equals(p.getStatus())
                            && p.getCreatedAt() != null
                            && p.getCreatedAt().toLocalDate().isEqual(day))
                    .map(PurchaseResponse::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(MonthlyDataPoint.builder().month(day.format(formatter)).sales(sales).purchases(spent).build());
            current = current.plusDays(1);
        }

        return result;
    }
}
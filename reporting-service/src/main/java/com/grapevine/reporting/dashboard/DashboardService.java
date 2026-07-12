package com.grapevine.reporting.dashboard;

import com.grapevine.reporting.client.InventoryClient;
import com.grapevine.reporting.client.ProductClient;
import com.grapevine.reporting.client.PurchaseClient;
import com.grapevine.reporting.client.SalesClient;
import com.grapevine.reporting.client.dto.OrderResponse;
import com.grapevine.reporting.client.dto.WarehouseStockResponse;
import com.grapevine.reporting.dashboard.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductClient  productClient;
    private final SalesClient    salesClient;
    private final PurchaseClient purchaseClient;
    private final InventoryClient inventoryClient;

    @Cacheable("dashboard-metrics")
    public DashboardResponse getMetrics() {

        Long totalProducts = (long) productClient.findAll().size();
        List<OrderResponse> orders = salesClient.findAll();
        Long totalOrders = (long) orders.size();
        Long totalPurchases = (long) purchaseClient.findAll().size();

        BigDecimal totalSales = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .map(OrderResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate today = LocalDate.now();
        BigDecimal todaySales = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus())
                        && o.getCreatedAt() != null
                        && o.getCreatedAt().toLocalDate().isEqual(today))
                .map(OrderResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long lowStockProducts = countLowStockProducts();

        return DashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalPurchases(totalPurchases)
                .totalSales(totalSales)
                .todaySales(todaySales)
                .lowStockProducts(lowStockProducts)
                .build();
    }

    private Long countLowStockProducts() {
        List<WarehouseStockResponse> allStock = inventoryClient.getAllStock();

        Map<Long, Integer> totalByProduct = allStock.stream()
                .collect(Collectors.groupingBy(
                        WarehouseStockResponse::getProductId,
                        Collectors.summingInt(WarehouseStockResponse::getStock)
                ));

        return totalByProduct.values().stream()
                .filter(stock -> stock <= 5)
                .count();
    }
}
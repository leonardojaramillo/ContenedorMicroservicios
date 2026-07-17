package com.grapevine.reporting.dashboard;

import com.grapevine.reporting.client.InventoryClient;
import com.grapevine.reporting.client.ProductClient;
import com.grapevine.reporting.client.PurchaseClient;
import com.grapevine.reporting.client.SalesClient;
import com.grapevine.reporting.client.dto.OrderResponse;
import com.grapevine.reporting.client.dto.WarehouseStockResponse;
import com.grapevine.reporting.dashboard.dto.AdminDashboardResponse;
import com.grapevine.reporting.dashboard.dto.CajeroDashboardResponse;
import com.grapevine.reporting.dashboard.dto.LogisticaDashboardResponse;
import com.grapevine.reporting.dashboard.dto.VendedorDashboardResponse;
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

    public Object getMetrics(String role) {
        return switch (role) {
            case "ADMIN", "SOFTWARE_ENGINEER" -> getAdminMetrics();
            case "CAJERO" -> getCajeroMetrics();
            case "LOGISTICA" -> getLogisticaMetrics();
            case "VENDEDOR" -> getVendedorMetrics();
            default -> getVendedorMetrics();
        };
    }

    @Cacheable("dashboard-admin")
    public AdminDashboardResponse getAdminMetrics() {
        List<OrderResponse> orders = salesClient.findAll();

        return AdminDashboardResponse.builder()
                .totalProducts((long) productClient.findAll().size())
                .totalOrders((long) orders.size())
                .totalPurchases((long) purchaseClient.findAll().size())
                .totalSales(sumPaid(orders, null))
                .todaySales(sumPaid(orders, LocalDate.now()))
                .lowStockProducts(countLowStockProducts())
                .build();
    }

    @Cacheable("dashboard-cajero")
    public CajeroDashboardResponse getCajeroMetrics() {
        List<OrderResponse> orders = salesClient.findAll();

        return CajeroDashboardResponse.builder()
                .totalOrders((long) orders.size())
                .totalSales(sumPaid(orders, null))
                .todaySales(sumPaid(orders, LocalDate.now()))
                .build();
    }

    @Cacheable("dashboard-logistica")
    public LogisticaDashboardResponse getLogisticaMetrics() {
        return LogisticaDashboardResponse.builder()
                .totalProducts((long) productClient.findAll().size())
                .lowStockProducts(countLowStockProducts())
                .build();
    }

    @Cacheable("dashboard-vendedor")
    public VendedorDashboardResponse getVendedorMetrics() {
        List<OrderResponse> orders = salesClient.findAll();

        return VendedorDashboardResponse.builder()
                .totalProducts((long) productClient.findAll().size())
                .totalOrders((long) orders.size())
                .todaySales(sumPaid(orders, LocalDate.now()))
                .build();
    }

    private BigDecimal sumPaid(List<OrderResponse> orders, LocalDate onlyDate) {
        return orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .filter(o -> onlyDate == null
                        || (o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().isEqual(onlyDate)))
                .map(OrderResponse::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
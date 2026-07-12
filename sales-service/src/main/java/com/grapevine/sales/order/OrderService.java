package com.grapevine.sales.order;
import com.grapevine.sales.client.InventoryClient;
import com.grapevine.sales.client.ProductClient;
import com.grapevine.sales.client.dto.AdjustStockRequest;
import com.grapevine.sales.client.dto.ProductResponse;
import com.grapevine.sales.client.dto.WarehouseResponse;
import com.grapevine.sales.client.dto.WarehouseStockResponse;
import com.grapevine.sales.order.dto.CreateOrderItemRequest;
import com.grapevine.sales.order.dto.CreateOrderRequest;
import com.grapevine.sales.order.dto.OrderDetailResponse;
import com.grapevine.sales.order.dto.OrderResponse;
import com.grapevine.sales.client.AuditClient;
import com.grapevine.sales.client.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient   productClient;
    private final InventoryClient inventoryClient;
    private final AuditClient auditClient;

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {

        WarehouseResponse warehouse = productClient.getWarehouse(request.getWarehouseId());

        List<WarehouseStockResponse> currentStocks =
                inventoryClient.getStockByWarehouse(request.getWarehouseId());

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .customerDocument(request.getCustomerDocument())
                .warehouseId(request.getWarehouseId())
                .status(OrderStatus.PAID)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderDetail> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequest item : request.getItems()) {

            ProductResponse product = productClient.getProduct(item.getProductId());

            int currentStock = currentStocks.stream()
                    .filter(ws -> ws.getProductId().equals(item.getProductId()))
                    .map(WarehouseStockResponse::getStock)
                    .findFirst()
                    .orElse(0);

            if (currentStock < item.getQuantity()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + product.getName()
                                + " (disponible en " + warehouse.getName() + ": " + currentStock + ")");
            }

            AdjustStockRequest adjustRequest = new AdjustStockRequest();
            adjustRequest.setProductId(product.getId());
            adjustRequest.setWarehouseId(request.getWarehouseId());
            adjustRequest.setNewStock(currentStock - item.getQuantity());
            adjustRequest.setReason("Venta a " + request.getCustomerName());
            inventoryClient.adjustStock(adjustRequest);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);

            details.add(OrderDetail.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .subtotal(subtotal)
                    .build());
        }

        order.setTotal(total);
        order.setDetails(details);

        Order savedOrder = orderRepository.save(order);

        try {
            auditClient.record(new CreateAuditLogRequest("VENTA_CREADA", "Venta registrada para: " + request.getCustomerName() + " por S/ " + total, null));
        } catch (Exception ignored) {}

        return toResponse(savedOrder, warehouse.getName());
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(o -> toResponse(o, resolveWarehouseName(o.getWarehouseId())))
                .toList();
    }

    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return toResponse(order, resolveWarehouseName(order.getWarehouseId()));
    }

    public List<OrderResponse> findByCustomerDocument(String document) {
        return orderRepository.findByCustomerDocumentOrderByCreatedAtDesc(document).stream()
                .map(o -> toResponse(o, resolveWarehouseName(o.getWarehouseId())))
                .toList();
    }

    @Transactional
    public OrderResponse cancel(Long id, String reason) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("La venta ya está anulada");
        }

        if (order.getWarehouseId() != null) {
            List<WarehouseStockResponse> currentStocks =
                    inventoryClient.getStockByWarehouse(order.getWarehouseId());

            for (OrderDetail detail : order.getDetails()) {
                int currentStock = currentStocks.stream()
                        .filter(ws -> ws.getProductId().equals(detail.getProductId()))
                        .map(WarehouseStockResponse::getStock)
                        .findFirst()
                        .orElse(0);

                AdjustStockRequest adjustRequest = new AdjustStockRequest();
                adjustRequest.setProductId(detail.getProductId());
                adjustRequest.setWarehouseId(order.getWarehouseId());
                adjustRequest.setNewStock(currentStock + detail.getQuantity());
                adjustRequest.setReason("Anulación de venta #" + order.getId());
                inventoryClient.adjustStock(adjustRequest);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelledAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        try {
            auditClient.record(new CreateAuditLogRequest("VENTA_ANULADA", "Venta #" + saved.getId() + " anulada: " + reason, null));
        } catch (Exception ignored) {}

        return toResponse(saved, resolveWarehouseName(saved.getWarehouseId()));
    }

    private String resolveWarehouseName(Long warehouseId) {
        if (warehouseId == null) return "—";
        try {
            return productClient.getWarehouse(warehouseId).getName();
        } catch (Exception e) {
            return "Almacén no disponible";
        }
    }

    private OrderResponse toResponse(Order order, String warehouseName) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerDocument(order.getCustomerDocument())
                .warehouseName(warehouseName)
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .cancelReason(order.getCancelReason())
                .cancelledAt(order.getCancelledAt())
                .details(order.getDetails() != null ? order.getDetails().stream()
                                                      .map(detail -> OrderDetailResponse.builder()
                                                                     .productId(detail.getProductId())
                                                                     .productName(detail.getProductName())
                                                                     .quantity(detail.getQuantity())
                                                                     .price(detail.getPrice())
                                                                     .subtotal(detail.getSubtotal())
                                                                     .build())
                                                      .toList() : List.of())
                .build();
    }
}
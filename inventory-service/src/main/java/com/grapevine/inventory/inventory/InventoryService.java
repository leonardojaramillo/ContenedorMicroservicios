package com.grapevine.inventory.inventory;

import com.grapevine.inventory.client.ProductClient;
import com.grapevine.inventory.client.dto.ProductResponse;
import com.grapevine.inventory.client.dto.WarehouseResponse;
import com.grapevine.inventory.inventory.dto.AdjustStockRequest;
import com.grapevine.inventory.inventory.dto.InventoryAdjustmentResponse;
import com.grapevine.inventory.warehouse_stock.WarehouseStock;
import com.grapevine.inventory.warehouse_stock.WarehouseStockRepository;
import com.grapevine.inventory.warehouse_stock.WarehouseStockResponse;
import com.grapevine.inventory.client.AuditClient;
import com.grapevine.inventory.client.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryAdjustmentRepository adjustmentRepository;
    private final WarehouseStockRepository      warehouseStockRepository;
    private final ProductClient                 productClient;
    private final AuditClient auditClient;

    public List<InventoryAdjustmentResponse> findAll() {
        return adjustmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventoryAdjustmentResponse> findByWarehouse(Long warehouseId) {
        return adjustmentRepository.findByWarehouseId(warehouseId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WarehouseStockResponse> getStockByWarehouse(Long warehouseId) {
        return warehouseStockRepository.findByWarehouseId(warehouseId).stream()
                .map(this::toStockResponse)
                .toList();
    }

    public List<WarehouseStockResponse> getAllStock() {
        return warehouseStockRepository.findAll().stream()
                .map(this::toStockResponse)
                .toList();
    }

    public InventoryAdjustmentResponse adjust(AdjustStockRequest request) {
        // Validamos que el producto y el almacén existan de verdad en product-service
        productClient.getProduct(request.getProductId());
        productClient.getWarehouse(request.getWarehouseId());

        WarehouseStock ws = warehouseStockRepository
                .findByWarehouseIdAndProductId(request.getWarehouseId(), request.getProductId())
                .orElse(WarehouseStock.builder()
                        .warehouseId(request.getWarehouseId())
                        .productId(request.getProductId())
                        .stock(0)
                        .build());

        Integer previousStock = ws.getStock();
        ws.setStock(request.getNewStock());
        warehouseStockRepository.save(ws);

        InventoryAdjustment adjustment = InventoryAdjustment.builder()
                .productId(request.getProductId())
                .warehouseId(request.getWarehouseId())
                .previousStock(previousStock)
                .newStock(request.getNewStock())
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .build();

        InventoryAdjustment saved = adjustmentRepository.save(adjustment);
        try {
            auditClient.record(new CreateAuditLogRequest("INVENTARIO_AJUSTADO", "Ajuste de stock: " + request.getReason(), null));
        } catch (Exception ignored) {}


        return toResponse(saved);
    }

    private InventoryAdjustmentResponse toResponse(InventoryAdjustment a) {
        String productName = safeProductName(a.getProductId());
        String warehouseName = safeWarehouseName(a.getWarehouseId());

        return InventoryAdjustmentResponse.builder()
                .id(a.getId())
                .productName(productName)
                .warehouseName(warehouseName)
                .previousStock(a.getPreviousStock())
                .newStock(a.getNewStock())
                .reason(a.getReason())
                .createdAt(a.getCreatedAt())
                .build();
    }

    private WarehouseStockResponse toStockResponse(WarehouseStock ws) {
        String productName = safeProductName(ws.getProductId());
        String warehouseName = safeWarehouseName(ws.getWarehouseId());
        String category = null;
        try {
            category = productClient.getProduct(ws.getProductId()).getCategory();
        } catch (Exception ignored) {}

        return WarehouseStockResponse.builder()
                .warehouseStockId(ws.getId())
                .warehouseId(ws.getWarehouseId())
                .warehouseName(warehouseName)
                .productId(ws.getProductId())
                .productName(productName)
                .productCategory(category)
                .stock(ws.getStock())
                .build();
    }

    private String safeProductName(Long productId) {
        try {
            ProductResponse p = productClient.getProduct(productId);
            return p.getName();
        } catch (Exception e) {
            return "Producto no disponible";
        }
    }

    private String safeWarehouseName(Long warehouseId) {
        if (warehouseId == null) return "—";
        try {
            WarehouseResponse w = productClient.getWarehouse(warehouseId);
            return w.getName();
        } catch (Exception e) {
            return "Almacén no disponible";
        }
    }
}
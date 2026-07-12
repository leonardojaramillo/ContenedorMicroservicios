package com.grapevine.inventory.inventory;

import com.grapevine.inventory.inventory.dto.AdjustStockRequest;
import com.grapevine.inventory.inventory.dto.InventoryAdjustmentResponse;
import com.grapevine.inventory.warehouse_stock.WarehouseStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LOGISTICA', 'SOFTWARE_ENGINEER')")
    public List<InventoryAdjustmentResponse> findAll(
            @RequestParam(required = false) Long warehouseId) {
        if (warehouseId != null) return inventoryService.findByWarehouse(warehouseId);
        return inventoryService.findAll();
    }

    @GetMapping("/stock")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'LOGISTICA', 'SOFTWARE_ENGINEER')")
    public List<WarehouseStockResponse> getStock(
            @RequestParam(required = false) Long warehouseId) {
        if (warehouseId != null) return inventoryService.getStockByWarehouse(warehouseId);
        return inventoryService.getAllStock();
    }

    @PostMapping("/adjust")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public InventoryAdjustmentResponse adjust(@RequestBody AdjustStockRequest request) {
        return inventoryService.adjust(request);
    }
}
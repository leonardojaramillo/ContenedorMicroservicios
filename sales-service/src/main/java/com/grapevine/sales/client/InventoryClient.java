package com.grapevine.sales.client;

import com.grapevine.sales.client.dto.AdjustStockRequest;
import com.grapevine.sales.client.dto.WarehouseStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/stock")
    List<WarehouseStockResponse> getStockByWarehouse(@RequestParam("warehouseId") Long warehouseId);

    @PostMapping("/api/inventory/adjust")
    void adjustStock(@RequestBody AdjustStockRequest request);
}
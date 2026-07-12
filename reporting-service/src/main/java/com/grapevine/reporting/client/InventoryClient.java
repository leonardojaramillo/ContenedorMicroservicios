package com.grapevine.reporting.client;

import com.grapevine.reporting.client.dto.WarehouseStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/stock")
    List<WarehouseStockResponse> getAllStock();
}
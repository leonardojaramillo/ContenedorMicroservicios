package com.grapevine.sales.client;

import com.grapevine.sales.client.dto.AdjustStockRequest;
import com.grapevine.sales.client.dto.ProductResponse;
import com.grapevine.sales.client.dto.WarehouseResponse;
import com.grapevine.sales.client.dto.WarehouseStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @GetMapping("/api/warehouses/{id}")
    WarehouseResponse getWarehouse(@PathVariable("id") Long id);
}
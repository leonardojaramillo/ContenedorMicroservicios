package com.grapevine.inventory.client;

import com.grapevine.inventory.client.dto.ProductResponse;
import com.grapevine.inventory.client.dto.WarehouseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @GetMapping("/api/warehouses/{id}")
    WarehouseResponse getWarehouse(@PathVariable("id") Long id);
}
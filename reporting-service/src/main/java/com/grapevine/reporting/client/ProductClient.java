package com.grapevine.reporting.client;

import com.grapevine.reporting.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products")
    List<ProductResponse> findAll();
}
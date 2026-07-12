package com.grapevine.reporting.client;

import com.grapevine.reporting.client.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "sales-service")
public interface SalesClient {

    @GetMapping("/api/orders")
    List<OrderResponse> findAll();
}
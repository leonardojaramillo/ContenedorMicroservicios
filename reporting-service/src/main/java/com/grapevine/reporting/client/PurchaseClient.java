package com.grapevine.reporting.client;

import com.grapevine.reporting.client.dto.PurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "purchase-service")
public interface PurchaseClient {

    @GetMapping("/api/purchases")
    List<PurchaseResponse> findAll();
}
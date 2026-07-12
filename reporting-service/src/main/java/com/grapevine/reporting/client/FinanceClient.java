package com.grapevine.reporting.client;

import com.grapevine.reporting.client.dto.CashRegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "finance-service")
public interface FinanceClient {

    @GetMapping("/api/cash/registers")
    List<CashRegisterResponse> getAllRegisters();
}
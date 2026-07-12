package com.grapevine.finance.client;

import com.grapevine.finance.client.dto.CreateAuditLogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "audit-service")
public interface AuditClient {

    @PostMapping("/api/audit-logs")
    void record(@RequestBody CreateAuditLogRequest request);
}
package com.grapevine.audit;

import com.grapevine.audit.dto.AuditLogResponse;
import com.grapevine.audit.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SOFTWARE_ENGINEER')")
    public List<AuditLogResponse> findAll(@RequestParam(required = false) String action,
                                          @RequestParam(required = false) String from,
                                          @RequestParam(required = false) String to) {
        return auditLogService.findAll(action, from, to);
    }

    @PostMapping
    public ResponseEntity<Void> record(@RequestBody CreateAuditLogRequest request) {
        auditLogService.record(request);
        return ResponseEntity.noContent().build();
    }
}
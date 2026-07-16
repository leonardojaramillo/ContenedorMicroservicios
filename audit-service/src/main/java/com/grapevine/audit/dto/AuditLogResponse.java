package com.grapevine.audit.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AuditLogResponse {
    private String id;
    private String action;
    private String description;
    private String performedBy;
    private String performedByEmail;
    private Instant createdAt;
}
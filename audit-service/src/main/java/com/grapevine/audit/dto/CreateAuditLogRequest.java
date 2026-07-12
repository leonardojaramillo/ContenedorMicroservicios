package com.grapevine.audit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuditLogRequest {
    private String action;
    private String description;
    private String performedBy;
}
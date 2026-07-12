package com.grapevine.auth.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuditLogRequest {
    private String action;
    private String description;
    private String performedBy;
}
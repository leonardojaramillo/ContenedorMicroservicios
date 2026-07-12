package com.grapevine.inventory.transfer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentRequest {
    private String  reason;
    private String  evidenceUrl;
    private Boolean stockRecoverable;
}
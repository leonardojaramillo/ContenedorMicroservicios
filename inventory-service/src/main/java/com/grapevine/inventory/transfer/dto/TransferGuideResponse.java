package com.grapevine.inventory.transfer.dto;

import com.grapevine.inventory.transfer.TransferGuideStatus;
import com.grapevine.inventory.transfer.TransferType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TransferGuideResponse {
    private Long                id;
    private TransferType        type;
    private TransferGuideStatus status;
    private String              originWarehouse;
    private Long                originWarehouseId;
    private String              destinationWarehouse;
    private Long                destinationWarehouseId;
    private String              description;
    private String              createdBy;
    private LocalDateTime       createdAt;
    private LocalDateTime       updatedAt;
    private String              incidentReason;
    private String              incidentEvidenceUrl;
    private Boolean             stockRecoverable;
    private List<TransferGuideItemResponse> items;
}
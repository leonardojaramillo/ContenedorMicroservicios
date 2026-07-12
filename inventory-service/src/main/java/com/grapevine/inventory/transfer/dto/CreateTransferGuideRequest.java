package com.grapevine.inventory.transfer.dto;

import com.grapevine.inventory.transfer.TransferType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTransferGuideRequest {
    private TransferType type;
    private Long originWarehouseId;
    private Long destinationWarehouseId;
    private String description;
    private List<TransferGuideItemRequest> items;
}
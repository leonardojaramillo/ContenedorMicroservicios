package com.grapevine.inventory.transfer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferGuideItemRequest {
    private Long productId;
    private Integer quantity;
}
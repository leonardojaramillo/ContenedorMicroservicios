package com.grapevine.inventory.transfer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferGuideItemResponse {
    private Long    productId;
    private String  productName;
    private Integer quantity;
}
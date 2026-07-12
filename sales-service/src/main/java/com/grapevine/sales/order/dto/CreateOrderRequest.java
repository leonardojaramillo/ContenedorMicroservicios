package com.grapevine.sales.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private String customerName;
    private String customerDocument;
    private Long warehouseId;
    private List<CreateOrderItemRequest> items;
}
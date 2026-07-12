package com.grapevine.product.warehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWarehouseRequest {
    private String name;
    private String address;
    private String ubigeoCode;
    private String department;
    private String province;
    private String district;
}
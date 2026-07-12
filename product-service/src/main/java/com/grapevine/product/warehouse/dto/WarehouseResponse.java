package com.grapevine.product.warehouse.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseResponse {
    private Long    id;
    private String  name;
    private String  address;
    private Boolean active;
    private String  ubigeoCode;
    private String  department;
    private String  province;
    private String  district;
}
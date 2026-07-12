package com.grapevine.sales.client.dto;

import lombok.Data;

@Data
public class WarehouseResponse {
    private Long id;
    private String name;
    private String address;
    private Boolean active;
    private String ubigeoCode;
    private String department;
    private String province;
    private String district;
}
package com.vt.cms.warehouse.application.dto;

import lombok.Data;

@Data
public class WarehouseSellerinfo {
    private String fullname;
    private String phone;
    private String address;
    private String warehousecode;
    private String warehouseName;
    private Double latitude;
    private Double longitude;

}

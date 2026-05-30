package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class WarehouseRequest {
    private Integer warehouse_code;
    private String warehouse_name;
    private Integer phone_number;
    private Integer province_code;
    private Integer province_name;
    private Integer province_id;
    private String street;
    private String address_detail;
    private Integer ward_code;
    private String ward_name;
    private Integer ward_id;
    private boolean address_is_default;
}

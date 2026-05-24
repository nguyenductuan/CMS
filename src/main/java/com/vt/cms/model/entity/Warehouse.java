package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Warehouse {
    private Integer id;
    private Integer warehouse_code;
    private String warehouse_name;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String created_by;
    private String updated_by;
    private Integer phone_number;
    private Integer province_code;
    private Integer province_name;
    private Integer province_id;
    private String street;
    private String address_detail;
    private Integer ward_code;
    private String ward_name;
    private Integer ward_id;
    private boolean is_deleted;
}
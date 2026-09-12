package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product_Sku {
    private Integer id;
    private LocalDateTime created_at;
    private String created_by;
    private  boolean is_deleted;
    private LocalDateTime updated_at;
    private String updated_by;
     private String attribute;
     private String height;
     private String length;
     private  String width;
     private String weight;
     private  Integer productId;
     private  Integer stock;
     private String sku_code;
     private String status;
}

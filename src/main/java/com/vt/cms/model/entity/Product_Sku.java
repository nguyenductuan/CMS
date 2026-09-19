package com.vt.cms.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product_Sku {
    private Integer id;
    private LocalDateTime created_at;
    private String created_by;
    private  String is_deleted;
    private LocalDateTime updated_at;
    private String updated_by;
     private String attribute;
     private String height_cm;
     private String length_cm;
     private  String  width_cm;
     private String 	weight_gram;
     private  Integer product_id;
     private  Integer stock;
    private String type_product;
     private BigDecimal price;
     private  String image_url;
     private String sku_code;
     private String status;
}

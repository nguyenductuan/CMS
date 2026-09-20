package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private LocalDateTime created_at;
    private String created_by;
    private String 	is_deleted;
    private LocalDateTime updated_at;
    private String updated_by;
    private String description;
    private String json_images;
    private String json_attributes;// VD: [{"name":"Xanh than"},{"name":"Retro bụi"}]
    private String json_medias;
    private Integer price_display;
    private String status;
    private String json_attributes_name;// thuộc tính thứ nhất của sp VD: Màu
    private String type_products_name;//thuộc tính thứ 2 của sản phẩm. VD: Kích thươc
    private String type_products;//VD: [{"name":"M"},{"name":"L"}]
    private String cancel_at;
    private  String cancel_by;
    private  String approve_by;
    private  LocalDateTime approved_time;
}


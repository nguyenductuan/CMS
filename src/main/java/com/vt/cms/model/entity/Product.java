package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private Integer price_display;
    private Integer stock;
    private String description;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String created_by;
    private String updated_by;
    private String status;
    private String is_delete;
    private String json_image;
    private String json_attributes;// VD: [{"name":"Xanh than"},{"name":"Retro bụi"}]
    private String json_medias;
    private String json_attributes_name;// thuộc tính thứ nhất của sp VD: Màu
    private String type_products_name;//thuộc tính thứ 2 của sản phẩm. VD: Kích thươc
    private String type_products;//VD: [{"name":"M"},{"name":"L"}]
}


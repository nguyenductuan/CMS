package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private String createdBy;
    private String 	isDeleted;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String description;
    private String jsonImages;
    private String jsonAttributes;// VD: [{"name":"Xanh than"},{"name":"Retro bụi"}]
    private String jsonMedias;
    private String status;
    private String json_attributes_name;// thuộc tính thứ nhất của sp VD: Màu
    private String type_products_name;//thuộc tính thứ 2 của sản phẩm. VD: Kích thươc
    private String typeProducts;//VD: [{"name":"M"},{"name":"L"}]
    private String cancelAt;
    private  String cancelBy;
    private  String approveBy;
    private String approveByRole;
    private  LocalDateTime approvedTime;
}


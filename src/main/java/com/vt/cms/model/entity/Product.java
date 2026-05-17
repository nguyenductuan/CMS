package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private String image;
    private LocalDateTime createdAt;
    private String status;
}

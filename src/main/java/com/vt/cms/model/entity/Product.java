package com.vt.cms.model.entity;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String name;
    private Double price;
    private Integer stock;

}

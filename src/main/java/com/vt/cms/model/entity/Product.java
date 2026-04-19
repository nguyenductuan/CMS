package com.vt.cms.model.entity;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private Integer price;
    private Integer stock;

}

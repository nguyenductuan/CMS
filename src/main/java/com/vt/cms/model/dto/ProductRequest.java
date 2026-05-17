package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String productDescription;
    private Integer productPrice;
    private String productimage;
    private Integer stock;
}

package com.vt.cms.model.dto.product;

import lombok.Data;

@Data
public class SkuRequest {
    private String attribute;
    private String typeProduct;
    private String imageUrl;
    private String skuCode;
    private Integer price;
    private Integer stock;
    private String weightGram;
    private String lengthCm;
    private String widthCm;
    private String heightCm;
    private String status;
    private Integer attrIndex;
    private Integer typeProductIndex;


}

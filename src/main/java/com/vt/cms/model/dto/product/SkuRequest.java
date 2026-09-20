package com.vt.cms.model.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuRequest {
    private String attribute;
    private String type_product;
    private String image_url;
    private String sku_code;
    private BigDecimal price;
    private Integer stock;
    private String weight_gram;
    private String length_cm;
    private String width_cm;
    private String height_cm;
    private String status;
    private String attrIndex;
    private Integer typeProductIndex;


}

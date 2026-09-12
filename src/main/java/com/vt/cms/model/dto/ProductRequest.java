package com.vt.cms.model.dto;

import com.vt.cms.model.dto.product.*;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private String productName;
    private String productDescription;
    private Integer productPrice;
    private String productimage;
    private Integer stock;
    private List<String> categoryIds;
    private List<ImageRequest> images;
    private List<MediaRequest> medias;
    private List<AttributeRequest> attributes;
    private String attributesName;
    private List<TypeProductRequest> typeProducts;
    private String typeProductsName;
    private List<SkuRequest> skus;
    private String status;
}

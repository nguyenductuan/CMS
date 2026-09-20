package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vt.cms.model.dto.product.*;
import com.vt.cms.model.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {
    private  Integer id;
    private String name;
    private String description;
    private Integer stock;
    private List<ImageRequest> images;
    private List<MediaRequest> medias;
    private List<AttributeRequest> attributes;
    private String attributes_name;
    private List<TypeProductRequest> type_products;
    private String type_products_name;
    private List<SkuRequest> skus;
    private String status;


}

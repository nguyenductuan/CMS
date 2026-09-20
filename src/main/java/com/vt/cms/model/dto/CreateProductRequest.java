package com.vt.cms.model.dto;

import com.vt.cms.model.dto.product.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data

public class CreateProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    private String description;
    private Integer productPrice;
    private String productimage;
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

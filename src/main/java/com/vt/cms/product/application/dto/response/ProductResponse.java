package com.vt.cms.product.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.vt.cms.product.application.dto.product.AttributeRequest;
import com.vt.cms.product.application.dto.product.SkuRequest;
import com.vt.cms.product.application.dto.product.TypeProductRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    private  Integer id;
    private String name;
    private String description;
    @JsonIgnore
    private String jsonAttributesRaw;
    @JsonIgnore
    private String typeProductsRaw;
    private List<AttributeRequest> jsonAttributes;
    private List<TypeProductRequest> typeProducts;
    private String jsonAttributesName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String jsonImages;
    private String jsonMedias;
    private Long totalStock;
    private String createdBy;
    private String 	isDeleted;
    private String typeProductsName;
    private List<SkuRequest> skus;
    private String status;
    private String approveBy;
    private String approveByRole;
    private LocalDateTime approvedTime;
    private BigDecimal averageRating;
    private Long reviewCount;
    private List<ProductReviewResponse> reviews;
}

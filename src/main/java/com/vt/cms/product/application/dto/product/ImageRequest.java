package com.vt.cms.product.application.dto.product;

import lombok.Data;

@Data
public class ImageRequest {
    private String type;
    private String url;
    private Integer sort_order;
}

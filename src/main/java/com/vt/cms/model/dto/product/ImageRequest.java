package com.vt.cms.model.dto.product;

import lombok.Data;

@Data
public class ImageRequest {
    private String type;
    private String url;
    private Integer sort_order;
}

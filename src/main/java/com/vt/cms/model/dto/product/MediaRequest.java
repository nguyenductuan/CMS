package com.vt.cms.model.dto.product;

import lombok.Data;

@Data
public class MediaRequest {
    private String type;
    private String url;
    private Integer sort_order;
}

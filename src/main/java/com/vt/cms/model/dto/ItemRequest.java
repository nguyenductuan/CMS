package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class ItemRequest {
    private Integer productId;
    private  Integer campainId;
    private int quantity;
    private  Integer SkuId;

}

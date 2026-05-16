package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class OrdersRequest {

    private Integer pageNo = 0;

    private Integer pageSize = 10;

    private String status;

    private String keyword;

    public Integer getOffset() {
        return pageNo * pageSize;
    }
}

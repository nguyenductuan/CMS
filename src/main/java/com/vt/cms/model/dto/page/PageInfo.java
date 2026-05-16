package com.vt.cms.model.dto.page;

import lombok.Data;

@Data

public class PageInfo {
    private Integer pageNo;

    private Integer pageSize;

    private long totalCount;

    private Long totalPage;


}

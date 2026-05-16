package com.vt.cms.model.dto.page;

import lombok.Data;

@Data

public class PagingResponse<T> {
    private PageInfo pageInfo;
    private T data;
}

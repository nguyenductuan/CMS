package com.vt.cms.base.request;

import lombok.Data;

@Data
public class Pagerequest {

  private Integer pageNo = 0;

  private Integer pageSize = 10;

  private String status;

  private String keyword;

  public Integer getOffset() {
    return pageNo * pageSize;
  }
}

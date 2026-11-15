package com.vt.cms.campain.application.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class CampainProductRequest {
  private int productid;
  private List<CampainProductSku> skus;
}

package com.vt.cms.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CampainProductRequest {
    private int productid;
   private List<CampainProductSku> skus;
}
